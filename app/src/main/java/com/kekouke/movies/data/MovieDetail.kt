package com.kekouke.movies.data

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val name: String,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("year") val year: Int,
    @SerializedName("description") val description: String,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("countries") val countries: List<Country>,
)