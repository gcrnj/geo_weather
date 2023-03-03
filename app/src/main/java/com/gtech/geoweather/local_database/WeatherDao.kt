package com.gtech.geoweather.local_database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gtech.geoweather.models.WeatherDatabaseData

interface WeatherDao {

    @Query("SELECT * FROM weathers WHERE (email = :email) LIMIT 1")
    suspend fun getAll(): List<WeatherDatabaseData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(roomData: WeatherDatabaseData)
}