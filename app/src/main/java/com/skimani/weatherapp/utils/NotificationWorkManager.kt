package com.skimani.weatherapp.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.skimani.weatherapp.MainActivity
import com.skimani.weatherapp.R
import com.skimani.weatherapp.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class NotificationWorkManager @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val weatherRepository: WeatherRepository
) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        return try {
            Timber.d("PERIODIC WORK STarted<>>>>>>>>>>>>>>>>>>")
            val response1 = weatherRepository.getFavouriteCurrentWeatherNotification()
            val response = response1[0]
            if (response1.isNotEmpty()) {
                val city = response.city
                val cityId = response.cityId
                val temp = response.temperature
                val weather = response.weatherMain
                sendNotification(city, weather, temp, cityId)
            }
            Result.success()
        } catch (ex: Exception) {
            Timber.d("ERROR PERIODIC WORK")
            Result.failure()
        }
    }

    private fun sendNotification(city: String, weather: String, temp: String, cityId: Long) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("OPENED_FROM", "NOTIF")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        showNotification(
            "Weather Updates",
            "$city :: $weather, temperature $temp°",
            intent,
            cityId.toString()
        )
    }

    fun showNotification(
        title: String?,
        message: String?,
        intent: Intent?,
        push_notification_id: String
    ) {
        val resultPendingIntent: PendingIntent
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "com.skimani.weatherapp"
        val channelName = "Weather"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        var mBuilder: NotificationCompat.Builder? = null
        mBuilder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_ALL)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(intent)
        resultPendingIntent =
            PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(resultPendingIntent)
        mBuilder.setAutoCancel(true)
        notificationManager.notify(push_notification_id.toInt(), mBuilder.build())
    }
}
