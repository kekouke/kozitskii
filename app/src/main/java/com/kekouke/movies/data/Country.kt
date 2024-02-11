package com.kekouke.movies.data

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("country") val country: String
) {
    override fun toString(): String {
        return country
    }
}
