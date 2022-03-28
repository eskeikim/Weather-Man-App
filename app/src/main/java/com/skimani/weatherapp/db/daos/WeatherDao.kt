package com.skimani.weatherapp.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.db.entity.HourlyForecast

@Dao
interface WeatherDao {
    @Query("SELECT * FROM currentWeather ORDER BY favourite DESC, city ASC")
    fun getLocalCurrentWeatherTest(): List<CurrentWeather>

    @Query("SELECT * FROM currentWeather ORDER BY favourite DESC, city ASC")
    fun getLocalCurrentWeather(): LiveData<List<CurrentWeather>>

    @Query("SELECT * FROM currentWeather WHERE favourite=1 ORDER BY favourite DESC, city ASC")
    fun getFavouriteCurrentWeather(): LiveData<List<CurrentWeather>>

    @Query("SELECT * FROM hourlyForecast WHERE city=:city")
    fun getLocalHourlyForecast(city: String): LiveData<HourlyForecast>

    @Query("SELECT * FROM currentWeather WHERE city=:city AND country_code=:country")
    fun getCurrentWeatherDetails(city: String, country: String): LiveData<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentWeather(currentWeather: CurrentWeather)

    @Query("UPDATE currentWeather SET favourite=:isFavourite WHERE city=:city AND country_code=:country")
    suspend fun addFavourite(city: String, country: String, isFavourite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHourlyForecast(hourlyForecast: HourlyForecast)
}
