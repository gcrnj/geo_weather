package com.gtech.geoweather.models

data class WeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) {
    fun getWeatherDatabaseData(
    ): WeatherDatabaseData {
        return WeatherDatabaseData(
            main.temp,
            main.temp_min,
            main.temp_max,
            if (weather.isNotEmpty()) weather[0].description else "",
            if (weather.isNotEmpty()) weather[0].icon else "",
            wind.speed,
            timezone,
            sys.country,
            name,
            sys.sunrise,
            sys.sunset,
            dt
        )
    }
}