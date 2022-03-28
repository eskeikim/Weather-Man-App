package com.skimani.weatherapp.utils

class Constants {
    companion object {
        val API_KEY_VALUE = "89877fe690mshe5152e56afada42p106559jsnae164473e92a"
        val X_API_KEY = "X-RapidAPI-Key"
        val API_HOST_VALUE = "community-open-weather-map.p.rapidapi.com"
        val X_API_HOST = "X-RapidAPI-Host"

        val DATE_FORMAT_DISPLAY_12H = "hh:mm a"
        val DATE_FORMAT_LONG = "EEE, MMM d, yyyy"

        fun sampleCities(): ArrayList<String> {
            val cities = arrayListOf<String>()
            cities.add("Nairobi,ke")
            cities.add("Lagos,ng")
            cities.add("Tokyo,jp")
            cities.add("Mumbai,in")
            cities.add("Cairo,eg")
            cities.add("New York,us")
            cities.add("Mexico,mx")
            cities.add("Beijing,cn")
            cities.add("Madrid,es")
            cities.add("Dallas,us")
            cities.add("Baghdad,iq")
            cities.add("Abidjan,ci")
            cities.add("Harare,zw")
            cities.add("Zhongli,tw")
            cities.add("Mecca,sa")
            cities.add("Soweto,za")
            cities.add("Bucheon,kr")
            cities.add("Turin,it")
            cities.add("Sale,ma")
            cities.add("Butzbach,de")
            cities.add("Lens,fr")
            return cities
        }
    }
}
