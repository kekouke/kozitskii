package com.kekouke.movies.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("films") val movies: List<Movie>
) {
    override fun toString(): String {
        return "MovieResponse{" + "movies=" + movies + '}';
    }
}