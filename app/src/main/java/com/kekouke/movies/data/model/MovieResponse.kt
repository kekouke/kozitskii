package com.kekouke.movies.data.model

import com.google.gson.annotations.SerializedName
import com.kekouke.movies.data.model.Movie

data class MovieResponse(
    @SerializedName("films") val movies: List<Movie>,
    @SerializedName("pagesCount") val totalPages: Int
)