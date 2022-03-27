package com.skimani.weatherapp.utils

import androidx.appcompat.widget.AppCompatImageView
import com.skimani.weatherapp.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun getDateTime(time: Long, targetFormat: String): String? {
            return try {
                SimpleDateFormat(targetFormat, Locale.getDefault()).format(time * 1_000L)
            } catch (e: Exception) {
                Timber.d(e.localizedMessage, e); null
            }
        }

        fun getTimeFromDate(date1: Long, originalFormat: String): Long {
            val date = formatTimeZone(date1 * 1_000L)
            if (date != null) {
                return try {
                    if (date != "0001-01-01T00:00:00Z" && date.isNotEmpty()) SimpleDateFormat(
                        originalFormat,
                        Locale.getDefault()
                    ).let {
                        it.timeZone = TimeZone.getTimeZone("GMT+6")
                        it.parse(date)?.time ?: 0
                    } else 0
                } catch (e: Exception) {
                    Timber.d(e.localizedMessage, e); 0
                }
            } else {
                return 0L
            }
        }

        fun formatTimeZone(date: Long): String? {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ENGLISH)
            val formatedTime = sdf.format(Date(date).time)
            return formatedTime
        }

        fun convertFromKelvinToCelsius(temp: Double): String {
            val tempDouble = temp - 273.15
            val tempString = String.format("%.0f", tempDouble)
            return tempString
        }

        fun bindWeatherIcon(weather: String, ivWeatherIcon: AppCompatImageView) {
            when (weather) {
                "Clear" -> {
                    ivWeatherIcon.setImageDrawable(ivWeatherIcon.context.getDrawable(R.drawable.partly_cloudy))
                }
                "Clouds" -> {
                    ivWeatherIcon.setImageDrawable(ivWeatherIcon.context.getDrawable(R.drawable.clouds))
                }
                "Rains" -> {
                    ivWeatherIcon.setImageDrawable(ivWeatherIcon.context.getDrawable(R.drawable.rainy_night))
                }
                "Haze" -> {
                    ivWeatherIcon.setImageDrawable(ivWeatherIcon.context.getDrawable(R.drawable.haze))
                }
                else -> ivWeatherIcon.setImageDrawable(ivWeatherIcon.context.getDrawable(R.drawable.partly_cloudy))
            }
        }
    }
}
