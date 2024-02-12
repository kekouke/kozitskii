package com.kekouke.movies.data.converters

import androidx.room.TypeConverter
import com.kekouke.movies.data.model.Country

class CountryListConverter {
    @TypeConverter
    fun fromString(value: String): List<Country> {
        return value.split("," ).map { Country(it) }
    }

    @TypeConverter
    fun fromCountryList(countries: List<Country>): String {
        return countries.joinToString(",")
    }
}