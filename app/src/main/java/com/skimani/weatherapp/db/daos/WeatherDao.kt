package com.skimani.weatherapp.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skimani.weatherapp.db.entity.CurrentWeather

@Dao
interface WeatherDao {
    @Query("SELECT * FROM currentWeather")
    fun getLocalCurrentWeather(): LiveData<List<CurrentWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentWeather(currentWeather: CurrentWeather)
}
