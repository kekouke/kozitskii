package com.kekouke.movies.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movie_detail")
data class MovieDetail(
    @PrimaryKey @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val name: String,
    @SerializedName("posterUrl") val posterUrl: String,
    @SerializedName("year") val year: Int,
    @SerializedName("description") val description: String,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("countries") val countries: List<Country>,
) : Parcelable