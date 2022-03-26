package com.skimani.weatherapp.repository

import androidx.lifecycle.LiveData
import com.skimani.weatherapp.db.daos.WeatherDao
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.network.api.ApiService
import com.skimani.weatherapp.network.datasource.NetworkResult
import com.skimani.weatherapp.network.models.CurrentWeatherResponse
import com.skimani.weatherapp.utils.Constants
import com.skimani.weatherapp.utils.Util
import com.skimani.weatherapp.utils.safeApiCall
import timber.log.Timber
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val apiService: ApiService
) {

    /**
     * get current weather from local db
     */
    fun getLocalCurrentWeather(): LiveData<List<CurrentWeather>> {
        return weatherDao.getLocalCurrentWeather()
    }

    /**
     * Fetch current weather from internet
     */
    suspend fun getCurrentWeather(location: String) {
        val currentResponse = safeApiCall(apiCall = { apiService.getCurrentWeather(location) })
        getData(currentResponse)
    }

    private suspend fun getData(currentResponse: NetworkResult<CurrentWeatherResponse>) {
        when (currentResponse) {
            is NetworkResult.Success -> {
                saveLocationCurrentWeather(currentResponse.data)
            }
            is NetworkResult.Error -> currentResponse.exception
            is NetworkResult.NetworkError -> currentResponse.exception
        }
    }

    private suspend fun saveLocationCurrentWeather(data: CurrentWeatherResponse) {
        Timber.d("DD $data")
        val weatherMain = data.weather[0].main
        val weatherIcon = data.weather[0].icon
        val weatherDesc = data.weather[0].description
        val date = Util.getDateTime(data.dt.toLong(), Constants.DATE_FORMAT_LONG) ?: ""
        val dateLong = Util.getTimeFromDate(date, Constants.DATE_FORMAT_LONG)
        val dateFormatted = Util.getDateTime(dateLong, Constants.DATE_FORMAT_LONG)
        val currentWeather = CurrentWeather(
            cityId = data.sys.id.toLong(),
            visibility = data.visibility,
            humidity = data.main.humidity,
            temperature = data.main.temp,
            pressure = data.main.pressure,
            city = data.name,
            date = dateFormatted ?: "",
            time = Util.getDateTime(data.dt.toLong(), Constants.DATE_FORMAT_DISPLAY_12H) ?: "",
            weatherMain = weatherMain,
            weatherIcon = weatherIcon,
            weatherDescription = weatherDesc,
            windSpeed = data.wind.speed,
            countryCode = data.sys.country

        )
        weatherDao.saveCurrentWeather(currentWeather)
    }
}
