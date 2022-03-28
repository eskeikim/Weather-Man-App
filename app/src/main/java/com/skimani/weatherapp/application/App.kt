package com.skimani.weatherapp.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {

        /**
         * Initialize Timber Logger for debug build
         */
//        if (BuildConfig.DEBUG) {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return String.format(
                    "Class:%s: Line: %s, Method: %s",
                    super.createStackElementTag(element),
                    element.lineNumber,
                    element.methodName
                )
            }
        })
//        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
