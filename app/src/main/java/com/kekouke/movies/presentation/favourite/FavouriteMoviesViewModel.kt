package com.kekouke.movies.presentation.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kekouke.movies.data.model.Movie
import com.kekouke.movies.data.MovieRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class FavouriteMoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository.getInstance(application)

    val movies = repository.favouriteMovies

    fun changeFavouriteState(movie: Movie, inFavourite: Boolean) {
        if (inFavourite) {
            repository.removeFromFavourite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        } else {
            repository.addToFavourite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

}