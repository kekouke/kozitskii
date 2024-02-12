package com.kekouke.movies.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import com.kekouke.movies.data.datasource.LocalDataSource
import com.kekouke.movies.data.datasource.RemoteDataSource
import com.kekouke.movies.data.model.Movie
import com.kekouke.movies.data.model.MovieDetail
import com.kekouke.movies.data.model.MovieResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieRepository private constructor(application: Application) {

    private val kinopoiskAPI = RemoteDataSource().apiService
    private val localDataSource = LocalDataSource.getInstance(application)

    private var wasFetchMovies = false
    private val localMovies = mutableListOf<Movie>()
    private val moviesInFavouriteCache = mutableSetOf<Int>()

    val favouriteMovies = localDataSource.movieDao().getFavouriteMoviesLiveData().map {
        it.onEach {
            it.inFavourite = true
        }
    }
    val shouldReloadMovieList: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(favouriteMovies) {
            if (wasFetchMovies) {
                value = Unit
            }
        }
    }

    fun getMovies(page: Int): Single<MovieResponse>  {
        return kinopoiskAPI.getMovies(page)
            .map {
                if (!wasFetchMovies) {
                    fetchMovies()
                    wasFetchMovies = true
                }
                val movies = it.movies
                movies.forEach {
                    it.inFavourite = moviesInFavouriteCache.contains(it.id)
                }
                it
            }
    }

    private fun fetchMovies() {
        localMovies.addAll(localDataSource.movieDao().getFavouriteMovies())
        localMovies.forEach {
            moviesInFavouriteCache.add(it.id)
        }
    }

    fun getMovieDetailById(id: Int): Single<MovieDetail> {
        return kinopoiskAPI.getMovieDetailById(id)
    }

    fun getLocalMovieDetailById(id: Int): Single<MovieDetail> {
        return localDataSource.movieDetailDao().getMovieDetail(id)
    }

    fun addToFavourite(movie: Movie): Completable {
        val s = getMovieDetailById(movie.id)
            .subscribeOn(Schedulers.io())
            .map {
                localDataSource.movieDao().addMovieToFavourite(movie)
                localDataSource.movieDetailDao().addMovieDetailToFavourite(it)
                moviesInFavouriteCache.add(movie.id)
            }
        return Completable.fromSingle(s)
    }

    fun removeFromFavourite(movie: Movie): Completable {
        return Completable.fromAction {
            localDataSource.movieDao().removeMovieFromFavourite(movie.id)
            localDataSource.movieDetailDao().removeMovieDetailFromFavourite(movie.id)
            moviesInFavouriteCache.remove(movie.id)
        }
    }

    fun reloadMovieList(loadedMovies: MutableList<Movie>?): MutableList<Movie>? {
        return if (loadedMovies == null) {
            null
        } else {
            loadedMovies.map { it.copy(inFavourite = moviesInFavouriteCache.contains(it.id)) }.toMutableList()
        }
    }

    companion object {

        private var INSTANCE: MovieRepository? = null
        private val LOCK = Any()

        fun getInstance(application: Application): MovieRepository {
            INSTANCE?.let {
                return it
            }

            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val repository = MovieRepository(application)
                INSTANCE = repository
                return repository
            }
        }

    }
}