package com.kekouke.movies.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("filmId") val id: Int,
    @SerializedName("nameRu") val name: String,
    @SerializedName("year") val year: String,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String,
) {
    override fun toString(): String {
        return "Movie(id=$id, name='$name', year='$year', posterUrlPreview='$posterUrlPreview')"
    }
}