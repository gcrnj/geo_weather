package com.gtech.geoweather.sections.activity_landing_page

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gtech.geoweather.sections.fragment_wather_history.WeatherHistoryFragment
import com.gtech.geoweather.sections.fragment_weather_home.HomeWeatherFragment

class LandingPageViewModel : ViewModel() {

    val currentTabSelected = MutableLiveData<Int>()
    val fragments = MutableLiveData<List<Fragment>>()

    init {
        currentTabSelected.value = 0
        fragments.value = listOf(HomeWeatherFragment(), WeatherHistoryFragment())
    }
}