package com.skimani.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.network.models.CurrentWeatherResponse
import com.skimani.weatherapp.repository.WeatherRepository
import com.skimani.weatherapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val weatherRepository: WeatherRepository) : ViewModel() {

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse>
        get() = _currentWeather

    val localCurrentWeather: LiveData<List<CurrentWeather>>
        get() = localCurrentWeather()

    init {
//        getCurrentWeather()
    }

    fun localCurrentWeather() = weatherRepository.getLocalCurrentWeather()

    fun getCurrentWeather() {
        val cities = Constants.sampleCities()
        viewModelScope.launch {
            cities.forEach {
                val currentWeatherResponse = weatherRepository.getCurrentWeather(it)
            }
        }
    }

    fun addFavourite(city: String, country: String, isFavourite: Boolean) {
        viewModelScope.launch {
            weatherRepository.addFavourite(city, country, isFavourite)
        }
    }
}
