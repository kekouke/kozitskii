package com.kekouke.movies.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kekouke.movies.data.MovieDetail
import com.kekouke.movies.data.MovieRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailViewModel : ViewModel() {

    private val repository = MovieRepository()
    private val compositeDisposable = CompositeDisposable()

    private var _movieDetail: MutableLiveData<MovieDetail> = MutableLiveData()
    val movieDetail: LiveData<MovieDetail>
        get() = _movieDetail

    fun loadMovieDetail(id: Int) {
        val disposable = repository.getMovieDetailById(id)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}