package com.skimani.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skimani.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(val weatherRepository: WeatherRepository) : ViewModel() {

    fun addFavourite(city: String, country: String, isFavourite: Boolean) {
        viewModelScope.launch {
            weatherRepository.addFavourite(city, country, isFavourite)
        }
    }
}
