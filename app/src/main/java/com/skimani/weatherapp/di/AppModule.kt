package com.skimani.weatherapp.di

import android.content.Context
import com.skimani.weatherapp.db.AppDB
import com.skimani.weatherapp.db.daos.WeatherDao
import com.skimani.weatherapp.network.api.ApiService
import com.skimani.weatherapp.network.datasource.NetworkInterceptor
import com.skimani.weatherapp.network.datasource.RemoteDataSource
import com.skimani.weatherapp.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesAppDb(@ApplicationContext context: Context): AppDB {
        return AppDB.getInstance(context)
    }

    @Singleton
    @Provides
    fun providesWeatherDao(appDB: AppDB): WeatherDao {
        return appDB.weatherDao()
    }

    @Singleton
    @Provides
    fun providesNetworkInterceptors(@ApplicationContext context: Context): NetworkInterceptor {
        return NetworkInterceptor(context)
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        networkInterceptor: NetworkInterceptor
    ): RemoteDataSource {
        return RemoteDataSource(networkInterceptor)
    }

    @Singleton
    @Provides
    fun providesApiService(remoteDatSource: RemoteDataSource): ApiService {
        return remoteDatSource.buildApi(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesWeatherRepository(weatherDao: WeatherDao): WeatherRepository {
        return WeatherRepository(weatherDao)
    }
}
