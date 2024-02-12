package com.kekouke.movies.data

import com.kekouke.movies.data.model.MovieDetail
import com.kekouke.movies.data.model.MovieResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApiService {

    @Headers("X-API-KEY: 1e12507f-4f48-4941-992c-29e52506a215")
    @GET("v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    fun getMovies(@Query("page") page: Int): Single<MovieResponse>

    @Headers("X-API-KEY: 1e12507f-4f48-4941-992c-29e52506a215")
    @GET("v2.2/films/{id}")
    fun getMovieDetailById(@Path("id") id: Int): Single<MovieDetail>
}