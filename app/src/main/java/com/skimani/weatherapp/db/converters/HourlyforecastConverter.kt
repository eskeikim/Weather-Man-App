package com.skimani.weatherapp.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skimani.weatherapp.db.entity.Hourly
import java.lang.reflect.Type

class HourlyforecastConverter {
    val gson = Gson()

    @TypeConverter
    fun fromHourlyForecast(hourly: List<Hourly>): String {
        return gson.toJson(hourly)
    }

    @TypeConverter
    fun toHourlyForecast(json: String): List<Hourly> {
        val listType: Type = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(json, listType)
    }
}
