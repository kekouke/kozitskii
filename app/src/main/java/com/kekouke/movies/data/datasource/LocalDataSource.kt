package com.kekouke.movies.data.datasource

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kekouke.movies.data.converters.CountryListConverter
import com.kekouke.movies.data.converters.GenreListConverter
import com.kekouke.movies.data.model.Movie
import com.kekouke.movies.data.MovieDao
import com.kekouke.movies.data.model.MovieDetail
import com.kekouke.movies.data.MovieDetailDao

@Database(entities = [Movie::class, MovieDetail::class], version = 1)
@TypeConverters(GenreListConverter::class, CountryListConverter::class)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailDao

    companion object {

        private const val DB_NAME = "movies.db"

        private var INSTANCE: LocalDataSource? = null
        private val LOCK = Any()

        fun getInstance(application: Application): LocalDataSource {
            INSTANCE?.let {
                return it
            }

            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    LocalDataSource::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }

    }
}