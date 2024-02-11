package com.kekouke.movies.data

import io.reactivex.rxjava3.core.Single

class MovieRepository {

    private val kinopoiskAPI = MovieRemoteDataSource().apiService

    fun getMovies(page: Int): Single<MovieResponse> {
        return kinopoiskAPI.getMovies(page)
    }

    fun getMovieDetailById(id: Int): Single<MovieDetail> {
        return kinopoiskAPI.getMovieDetailById(id)
    }
}