package com.gtech.geoweather.sections.fragment_wather_history

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.gtech.geoweather.models.User
import com.gtech.geoweather.models.WeatherDatabaseData

class WeatherHistoryViewModel : ViewModel() {

    val weatherHistoryData = MediatorLiveData<Map<String, List<WeatherDatabaseData?>>>()
    val user = MediatorLiveData<User>()
}