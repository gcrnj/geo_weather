package com.gtech.geoweather.sections.fragment_weather_home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gtech.geoweather.models.WeatherResponse

class HomeWeatherViewModel : ViewModel() {

    val weatherUpdate = MutableLiveData<WeatherResponse>()
}