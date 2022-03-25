package com.skimani.weatherapp.network.models

import com.google.gson.annotations.SerializedName

data class DailyForecastReponse(
    @SerializedName("city")
    val city: City,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("message")
    val message: Double,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("list")
    val list: List<Hourly>
) {
    data class City(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("coord")
        val coord: Coord,
        @SerializedName("country")
        val country: String,
        @SerializedName("population")
        val population: Int,
        @SerializedName("timezone")
        val timezone: Int
    ) {
        data class Coord(
            @SerializedName("lon")
            val lon: Double,
            @SerializedName("lat")
            val lat: Double
        )
    }

    data class Hourly(
        @SerializedName("dt")
        val dt: Int,
        @SerializedName("sunrise")
        val sunrise: Int,
        @SerializedName("sunset")
        val sunset: Int,
        @SerializedName("temp")
        val temp: Temp,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike,
        @SerializedName("pressure")
        val pressure: Int,
        @SerializedName("humidity")
        val humidity: Int,
        @SerializedName("weather")
        val weather: List<Weather>,
        @SerializedName("speed")
        val speed: Double,
        @SerializedName("deg")
        val deg: Int,
        @SerializedName("gust")
        val gust: Double,
        @SerializedName("clouds")
        val clouds: Int,
        @SerializedName("pop")
        val pop: Double,
        @SerializedName("rain")
        val rain: Double
    ) {
        data class Temp(
            @SerializedName("day")
            val day: Double,
            @SerializedName("min")
            val min: Double,
            @SerializedName("max")
            val max: Double,
            @SerializedName("night")
            val night: Double,
            @SerializedName("eve")
            val eve: Double,
            @SerializedName("morn")
            val morn: Double
        )

        data class FeelsLike(
            @SerializedName("day")
            val day: Double,
            @SerializedName("night")
            val night: Double,
            @SerializedName("eve")
            val eve: Double,
            @SerializedName("morn")
            val morn: Double
        )

        data class Weather(
            @SerializedName("id")
            val id: Int,
            @SerializedName("main")
            val main: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("icon")
            val icon: String
        )
    }
}
