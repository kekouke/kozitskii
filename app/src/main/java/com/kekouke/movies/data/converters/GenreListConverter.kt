package com.kekouke.movies.data.converters

import androidx.room.TypeConverter
import com.kekouke.movies.data.model.Genre

class GenreListConverter {

    @TypeConverter
    fun fromString(value: String): List<Genre> {
        return value.split("," ).map { Genre(it) }
    }

    @TypeConverter
    fun fromCountryList(genres: List<Genre>): String {
        return genres.joinToString(",")
    }
}