package com.skimani.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skimani.weatherapp.network.datasource.NetworkResult
import com.skimani.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val weatherRepository: WeatherRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    init {
        getCurrentWeather()
    }

    private fun getCurrentWeather() {
        viewModelScope.launch {
            val currentWeatherResponse = weatherRepository.getCurrentWeather("nairobi,ke")
            when (currentWeatherResponse) {
                is NetworkResult.Success -> currentWeatherResponse.data
                is NetworkResult.Error -> currentWeatherResponse.exception
                is NetworkResult.NetworkError -> currentWeatherResponse.exception
            }
        }
    }
}
