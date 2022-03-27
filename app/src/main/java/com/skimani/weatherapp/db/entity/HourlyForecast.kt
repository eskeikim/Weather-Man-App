package com.skimani.weatherapp.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hourlyForecast", indices = [Index(value = ["city", "country_code", "city_id"])]
)
data class HourlyForecast(
    @PrimaryKey
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country_code") val country: String,
    @ColumnInfo(name = "hourly") val list: List<Hourly>?=null,
    @ColumnInfo(name = "city_id") val cityId: Long
)

data class Hourly(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "visibility") val visibility: Int,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "temp") val temperature: String,
    @ColumnInfo(name = "weather_main") val weatherMain: String,
    @ColumnInfo(name = "weather_description") val weatherDescription: String,
    @ColumnInfo(name = "weather_icon") val weatherIcon: String,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double,
    @ColumnInfo(name = "pressure") val pressure: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "dewPoint") val dewPoint: String
)
