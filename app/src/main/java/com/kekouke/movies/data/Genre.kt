package com.kekouke.movies.data

import com.google.gson.annotations.SerializedName

class Genre(
    @SerializedName("genre") val genre: String
) {
    override fun toString(): String {
        return "Genre(genre='$genre')"
    }
}
