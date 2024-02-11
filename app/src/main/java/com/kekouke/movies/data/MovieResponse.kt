package com.kekouke.movies.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("films") val movies: List<Movie>,
    @SerializedName("pagesCount") val totalPages: Int
) {
    override fun toString(): String {
        return "MovieResponse(movies=$movies, totalPages=$totalPages)"
    }
}