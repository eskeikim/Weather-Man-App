package com.skimani.weatherapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skimani.weatherapp.db.daos.WeatherDao
import com.skimani.weatherapp.db.entity.CurrentWeather

@Database(entities = [CurrentWeather::class], version = 2, exportSchema = true)
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
