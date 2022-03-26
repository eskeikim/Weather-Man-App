package com.skimani.weatherapp.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "currentWeather", indices = [Index(value = ["city", "country_code"])]
)
data class CurrentWeather(
    @PrimaryKey
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "city_id") val cityId: Long,
    @ColumnInfo(name = "visibility") val visibility: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "temp") val temperature: Double,
    @ColumnInfo(name = "country_code") val countryCode: String,
    @ColumnInfo(name = "weather_main") val weatherMain: String,
    @ColumnInfo(name = "weather_description") val weatherDescription: String,
    @ColumnInfo(name = "weather_icon") val weatherIcon: String,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "favourite") val favourite: Boolean = false
)
