package com.skimani.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(val weatherRepository: WeatherRepository) : ViewModel() {
//    private val _localCurrentWeatherDetails: MutableLiveData<CurrentWeather>()
//    val localCurrentWeatherDetails:LiveData<CurrentWeather>
//        get() = localCurrentWeatherDetails

    fun localCurrentWeatherDetails(current: CurrentWeather) =
        weatherRepository.getCurrentWeatherDetails(current.city, current.countryCode)

    fun addFavourite(city: String, country: String, isFavourite: Boolean) {
        viewModelScope.launch {
            weatherRepository.addFavourite(city, country, isFavourite)
        }
    }

    fun getLocalHourlyForecast(city: String) = weatherRepository.getLocalHourlyForecast(city)

    fun getHourlyForecast(city: String) {
        viewModelScope.launch {
//            weatherRepository.getHourlyForecast(city)
        }
    }
}
