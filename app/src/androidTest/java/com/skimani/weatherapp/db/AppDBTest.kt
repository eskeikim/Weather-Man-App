package com.skimani.weatherapp.d

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skimani.weatherapp.db.AppDB
import com.skimani.weatherapp.db.daos.WeatherDao
import com.skimani.weatherapp.db.entity.CurrentWeather
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDBTest : TestCase() {
    private lateinit var weatherDao: WeatherDao
    private lateinit var db: AppDB

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java).build()
        weatherDao=db.weatherDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun WriteAndReadWeather() = runBlocking {
        val weather = CurrentWeather(
            "Nairobi",
            899,
            78800,
            "Tue, 22 Mar 2022",
            "9:AM",
            "23",
            "ke",
            "Clouds",
            "Clouds",
            "2lon",
            402.2,
            500,
            700,
            false
        )
        weatherDao.saveCurrentWeather(weather)
        val currentWeather = weatherDao.getLocalCurrentWeatherTest()
        assertEquals(weather, currentWeather[0])
    }
}
