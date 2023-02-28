package com.gtech.geoweather.api
import com.gtech.geoweather.models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("weather")
    fun getCurrentWeather(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>
}
