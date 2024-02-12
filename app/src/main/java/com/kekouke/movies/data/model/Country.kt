package com.kekouke.movies.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    @SerializedName("country") val country: String
) : Parcelable {
    override fun toString(): String {
        return country
    }
}
