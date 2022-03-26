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

        fun getTimeFromDate(date: String?, originalFormat: String): Long {
            if (date != null) {
                return try {
                    if (date != "0001-01-01T00:00:00Z" && date.isNotEmpty()) SimpleDateFormat(
                        originalFormat,
                        Locale.getDefault()
                    ).let {
                        it.timeZone = TimeZone.getTimeZone("UTC")
                        it.parse(date)?.time ?: 0
                    } else 0
                } catch (e: Exception) {
                    Timber.d(e.localizedMessage, e);0
                }
            } else {
                return 0L
            }
        }
    }
}
