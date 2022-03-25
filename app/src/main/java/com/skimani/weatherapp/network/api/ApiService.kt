package com.skimani.weatherapp.network.api

import com.skimani.weatherapp.network.models.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * Fetch the current weater data
     */
    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("q") search: String
    ): Response<CurrentWeatherResponse>
}
