package com.skimani.weatherapp.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skimani.weatherapp.db.entity.CurrentWeather
import com.skimani.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    val localCurrentWeather: LiveData<List<CurrentWeather>>
        get() = getFavouriteCurrentWeather()

    fun getFavouriteCurrentWeather() = weatherRepository.getFavouriteCurrentWeather()

    fun addFavourite(city: String, country: String, isFavourite: Boolean) {
        viewModelScope.launch {
            weatherRepository.addFavourite(city, country, isFavourite)
        }
    }
}
