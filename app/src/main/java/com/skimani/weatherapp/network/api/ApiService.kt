package com.skimani.weatherapp.network.api

import com.skimani.weatherapp.network.models.CurrentWeatherResponse
import com.skimani.weatherapp.network.models.HourlyForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * Fetch the current weater data
     */
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") search: String
    ): Response<CurrentWeatherResponse>

    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("q") search: String
    ): Response<HourlyForecastResponse>
}
