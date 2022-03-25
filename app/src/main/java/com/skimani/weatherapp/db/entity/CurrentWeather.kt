package com.skimani.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class CurrentWeather(
    @SerializedName("base")
    val base: String,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("timezone")
    val timezone: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("cod")
    val cod: Int
) {
    @PrimaryKey(autoGenerate = true)
    var weatherId: Long = 0L
}
