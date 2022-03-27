package com.skimani.weatherapp.repository

import androidx.lifecycle.LiveData
import com.skimani.weatherapp.db.daos.WeatherDao
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.db.entity.Hourly
import com.skimani.weatherapp.db.entity.HourlyForecast
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
     * get hourly forecast from local db
     */
    fun getLocalHourlyForecast(city: String): LiveData<HourlyForecast> {
        return weatherDao.getLocalHourlyForecast(city)
    }

    /**
     * get current weather details from local db
     */
    fun getCurrentWeatherDetails(city: String, country: String): LiveData<CurrentWeather> {
        return weatherDao.getCurrentWeatherDetails(city, country)
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
        val dateFormatted = Util.getDateTime(data.dt.toLong(), Constants.DATE_FORMAT_LONG)
        val currentWeather = CurrentWeather(
            cityId = data.sys.id.toLong(),
            visibility = data.visibility/1000,
            humidity = data.main.humidity,
            temperature = Util.convertFromKelvinToCelsius(data.main.temp),
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

    suspend fun addFavourite(city: String, country: String, isFavourite: Boolean) {
        weatherDao.addFavourite(city, country, isFavourite)
    }

    /**
     * Fetch hourly weather from internet
     */
    suspend fun getHourlyForecast(location: String) {
        val hourlyResponse = safeApiCall(apiCall = { apiService.getHourlyForecast(location) })
        when (hourlyResponse) {
            is NetworkResult.Success -> {
                val city = hourlyResponse.data.city.name
                val cityId = hourlyResponse.data.city.id.toLong()
                val country = hourlyResponse.data.city.country
                val hourlList = ArrayList<Hourly>()
                for (hour in hourlyResponse.data.list) {
                    val dateFormatted =
                        Util.getDateTime(hour.dt.toLong(), Constants.DATE_FORMAT_LONG) ?: ""
                    val hourly = Hourly(
                        date = dateFormatted,
                        time = Util.getDateTime(hour.dt.toLong(), Constants.DATE_FORMAT_DISPLAY_12H)
                            ?: "",
                        temperature = Util.convertFromKelvinToCelsius(hour.main.temp),
                        weatherMain = hour.weather[0].main,
                        weatherDescription = hour.weather[0].description,
                        weatherIcon = hour.weather[0].icon,
                        visibility = hour.visibility,
                        windSpeed = hour.wind.speed,
                        pressure = hour.main.pressure,
                        humidity = hour.main.humidity,
                        dewPoint = Util.convertFromKelvinToCelsius(hour.main.tempMin)
                    )
                    hourlList.add(hourly)
                }
                val hourlyForecast = HourlyForecast(
                    city = city,
                    cityId = cityId,
                    country = country,
                    list = hourlList
                )
                weatherDao.saveHourlyForecast(hourlyForecast)
            }
            is NetworkResult.NetworkError -> {
                Timber.d("Error ${hourlyResponse.exception}")
            }
            is NetworkResult.Error -> {
                Timber.d("Error ${hourlyResponse.exception}")
            }
        }
    }
}
