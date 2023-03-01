package com.gtech.geoweather.local_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gtech.geoweather.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE (email = :mobileOrEmail OR mobileNumber = :mobileOrEmail) LIMIT 1")
    fun findByMobileOrEmailNumber(mobileOrEmail: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}