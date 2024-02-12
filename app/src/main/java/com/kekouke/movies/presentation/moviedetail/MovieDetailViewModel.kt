package com.kekouke.movies.presentation.moviedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kekouke.movies.data.model.MovieDetail
import com.kekouke.movies.data.MovieRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    private var _movieDetail: MutableLiveData<MovieDetail> = MutableLiveData()
    val movieDetail: LiveData<MovieDetail>
        get() = _movieDetail

    private var _error: MutableLiveData<Boolean> = MutableLiveData(false)
    val error: LiveData<Boolean>
        get() = _error

    fun loadMovieDetail(id: Int) {
        val disposable = repository.getMovieDetailById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                _error.value = false
            }
            .doOnError {
                _error.value = true
            }
            .subscribe(
                {
                    _movieDetail.value = it
                },
                {
                    Log.d("MovieDetailViewModel", it.toString())
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getLocalMovieDetail(movieId: Int) {
        val disposable = repository.getLocalMovieDetailById(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _movieDetail.value = it
                },
                {
                    Log.d("MovieDetailViewModel", it.toString())
                }
            )
        compositeDisposable.add(disposable)
    }

}