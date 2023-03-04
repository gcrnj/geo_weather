package com.gtech.geoweather.models

data class WeatherDatabaseData(
    val temperature: Double,
    val tem_min: Double,
    val temp_max: Double,
    val description: String,
    val icon: String,
    val windSpeed: Double,
    val timezone: Int,
    val country: String,
    val cityName: String,
    val sunrise: Int,
    val sunset: Int,
    val dateTime: Int
)