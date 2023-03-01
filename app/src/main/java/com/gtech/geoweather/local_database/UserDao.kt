package com.gtech.geoweather.local_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gtech.geoweather.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE (mobile_number = :mobile) LIMIT 1")
    suspend fun findByMobile(mobile: String): User?

    @Query("SELECT * FROM users WHERE (email = :email) LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
}