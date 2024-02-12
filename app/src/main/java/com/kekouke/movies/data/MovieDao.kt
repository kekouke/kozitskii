package com.kekouke.movies.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kekouke.movies.data.model.Movie

@Dao
interface MovieDao {

    @Insert
    fun addMovieToFavourite(movie: Movie)

    @Query("SELECT * FROM movie")
    fun getFavouriteMovies(): List<Movie>


    @Query("SELECT * FROM movie")
    fun getFavouriteMoviesLiveData(): LiveData<List<Movie>>

    @Query("DELETE FROM movie WHERE id = :id")
    fun removeMovieFromFavourite(id: Int)

}