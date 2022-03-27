package com.skimani.weatherapp.ui.notes

import androidx.lifecycle.ViewModel
import com.skimani.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel()
