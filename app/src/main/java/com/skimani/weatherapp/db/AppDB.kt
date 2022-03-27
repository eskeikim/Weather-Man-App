package com.skimani.weatherapp.db

import android.content.Context
import androidx.room.* // ktlint-disable no-wildcard-imports
import com.skimani.weatherapp.db.converters.HourlyforecastConverter
import com.skimani.weatherapp.db.daos.WeatherDao
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.db.entity.HourlyForecast

@Database(entities = [CurrentWeather::class, HourlyForecast::class], version = 3, exportSchema = true)
@TypeConverters(value = [HourlyforecastConverter::class])
abstract class AppDB : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        var INSTANCE: AppDB? = null
        fun getInstance(context: Context): AppDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }

        private fun buildDatabase(context: Context): AppDB =
            Room.databaseBuilder(context, AppDB::class.java, "weather_app_db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
