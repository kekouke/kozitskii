package com.kekouke.movies.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Genre(
    @SerializedName("genre") val genre: String
) : Parcelable {
    override fun toString(): String {
        return genre
    }
}
