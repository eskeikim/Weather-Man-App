package com.skimani.weatherapp.utils

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun getDateTime(time: Long, targetFormat: String): String? {
            return try {
                SimpleDateFormat(targetFormat, Locale.getDefault()).format(time)
            } catch (e: Exception) {
                Timber.d(e.localizedMessage, e); null
            }
        }
    }
}
