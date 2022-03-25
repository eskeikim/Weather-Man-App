package com.skimani.weatherapp.network.datasource

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor(context: Context) : Interceptor {
    val appContext = context.applicationContext
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NetworkException("Please check your internet connection")
        } else {
            val url = chain.request()
                .url
                .newBuilder()
                .build()

            val request = chain.request().newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun isNetworkAvailable(): Boolean {
        var result = false
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else {
//                TODO("VERSION.SDK_INT < M")
            }
        }
        return result
    }
}
