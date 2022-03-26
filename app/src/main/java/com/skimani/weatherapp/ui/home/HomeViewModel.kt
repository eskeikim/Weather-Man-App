package com.skimani.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.network.models.CurrentWeatherResponse
import com.skimani.weatherapp.repository.WeatherRepository
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
        val locations = arrayListOf<String>()
        locations.add("Nairobi,ke")
        locations.add("Kisumu,ke")
        locations.add("Mombasa,ke")
        locations.add("Lagos,ng")
        viewModelScope.launch {
            locations.forEach {
                val currentWeatherResponse = weatherRepository.getCurrentWeather(it)
            }
        }
    }
}
