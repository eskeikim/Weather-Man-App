package com.skimani.weatherapp.network.datasource

import com.skimani.weatherapp.BuildConfig
import com.skimani.weatherapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource(
    private val networkInterceptor: NetworkInterceptor
) {

    private fun getOkHttpClient(
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().also {
                        it.addHeader("Accept", "application/json")
                        it.addHeader("Content-Type", "application/json")
                        it.addHeader(Constants.X_API_HOST, Constants.API_HOST_VALUE)
                        it.addHeader(Constants.X_API_KEY, Constants.API_KEY_VALUE)
                    }.build()
                )
            }.also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    client.addInterceptor(logging)
                }
            }.build()
    }

    fun <Api> buildApi(api: Class<Api>): Api {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(getOkHttpClient(networkInterceptor))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

}

