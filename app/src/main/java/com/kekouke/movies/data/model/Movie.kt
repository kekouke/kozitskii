package com.kekouke.movies.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey @SerializedName("filmId") val id: Int,
    @SerializedName("nameRu") val name: String,
    @SerializedName("year") val year: String,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String,
    @SerializedName("posterUrl") val posterUrl: String,
    var inFavourite: Boolean = false
)