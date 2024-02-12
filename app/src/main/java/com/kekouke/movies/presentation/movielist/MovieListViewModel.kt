package com.kekouke.movies.presentation.movielist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kekouke.movies.data.model.Movie
import com.kekouke.movies.data.MovieRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    private var loadedMovies: MutableList<Movie>? = null
    private var _movies: MutableLiveData<List<Movie>> = MutableLiveData()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private var _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private var _error: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: LiveData<Boolean>
        get() = _error

    private var _addToFavouriteError: MutableLiveData<Boolean> = MutableLiveData()
    val addToFavouriteError: LiveData<Boolean>
        get() = _addToFavouriteError

    val shouldReloadMovieList = repository.shouldReloadMovieList

    private var currentPage = 1
    private var wasLastFetch = false

    init {
        loadMovies()
    }

    fun loadMovies() {
        if (_loading.value!! || wasLastFetch) {
            return
        }
        val disposable = repository.getMovies(currentPage++)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _loading.value = true
                _error.value = false
            }
            .doOnTerminate {
                _loading.value = false
            }
            .doOnError {
                _error.value = true
                currentPage--
            }
            .subscribe(
                {
                    wasLastFetch = currentPage > it.totalPages
                    if (loadedMovies == null) {
                        loadedMovies = mutableListOf()
                    }
                    loadedMovies?.addAll(it.movies)
                    _movies.value = loadedMovies
                },
                {
                    Log.d("MovieListViewModel", it.toString())
                }
            )
        compositeDisposable.add(disposable)
    }

    fun changeFavouriteState(movie: Movie, inFavourite: Boolean) {
        val disposable: Disposable
        if (inFavourite) {
            disposable = repository.removeFromFavourite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _addToFavouriteError.value = true }
                .doOnTerminate { _addToFavouriteError.value = false }
                .subscribe({}, {})
        } else {
            disposable = repository.addToFavourite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _addToFavouriteError.value = true }
                .doOnTerminate { _addToFavouriteError.value = false }
                .subscribe({}, {})
        }
        compositeDisposable.add(disposable)
    }

    fun reloadMovieList() {
        loadedMovies = repository.reloadMovieList(loadedMovies)
        _movies.value = loadedMovies
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}