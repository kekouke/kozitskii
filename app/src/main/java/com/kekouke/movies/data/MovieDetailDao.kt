package com.kekouke.movies.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kekouke.movies.data.model.MovieDetail
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDetailDao {

    @Insert
    fun addMovieDetailToFavourite(movie: MovieDetail)

    @Query("SELECT * FROM movie_detail WHERE id = :id")
    fun getMovieDetail(id: Int): Single<MovieDetail>

    @Query("DELETE FROM movie_detail WHERE id = :id")
    fun removeMovieDetailFromFavourite(id: Int)

}