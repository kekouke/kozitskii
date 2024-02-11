package com.kekouke.movies.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kekouke.movies.data.Movie
import com.kekouke.movies.data.MovieDetail
import com.kekouke.movies.data.MovieRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val repository = MovieRepository()
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
                    Log.d("MainViewModel", "error")
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}