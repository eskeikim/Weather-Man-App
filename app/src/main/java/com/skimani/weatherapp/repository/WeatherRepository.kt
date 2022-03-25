package com.skimani.weatherapp.repository

import com.skimani.weatherapp.db.daos.WeatherDao
import com.skimani.weatherapp.network.api.ApiService
import com.skimani.weatherapp.network.datasource.NetworkResult
import com.skimani.weatherapp.network.models.CurrentWeatherResponse
import com.skimani.weatherapp.utils.safeApiCall
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val apiService: ApiService
) {

    /**
     * Fetch current weather
     */
    suspend fun getCurrentWeather(location: String): NetworkResult<CurrentWeatherResponse> {
        return safeApiCall(apiCall = { apiService.getCurrentWeather(location) })
    }
}
