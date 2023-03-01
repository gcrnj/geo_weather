package com.gtech.geoweather.models

import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true), Index(
        value = ["mobile_number"],
        unique = true
    )]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "middle_name") val middleName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "mobile_number") val mobileNumber: String,
    @ColumnInfo(name = "hashed_password") val hashedPassword: String
) {

    // Will return null if the user is validated
    fun errors(): User? {
        //Error Messages
        var eFirstName = ""
        var eMiddleName = ""
        var eLastName = ""
        var eEmail = ""
        var eMobileNumber = ""
        var validated = true
        if (firstName.isBlank()) {
            eFirstName = "First name cannot be blank"
            validated = false
        } else if (firstName.length < 2) {
            eFirstName = "First name cannot be less than 2 characters"
            validated = false
        }

        if (middleName.isNotBlank() && middleName.length < 2) {
            eMiddleName = "Please enter your full middle name"
            validated = false
        }

        if (lastName.isBlank()) {
            eLastName = "Last name cannot be blank"
            validated = false
        } else if (lastName.length < 2) {
            eLastName = "Last name cannot be less than 2 characters"
            validated = false
        }

        if (!isValidEmail()) {
            eEmail = "Please enter a valid email address"
            validated = false
        }

        if (!isValidMobileNumber()) {
            eMobileNumber = "Please enter a valid Philippine phone number"
            validated = false
        }

        // Return null if not validated, otherwise return user with errors
        return if (validated) null
        else User(0, eFirstName, eMiddleName, eLastName, eEmail, eMobileNumber, "null")
    }

    private fun isValidEmail(): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidMobileNumber(): Boolean {
        return mobileNumber.length == 10 && mobileNumber.firstOrNull() == '9'
    }


    companion object {
        val PASSWORD = 0
        val CONFIRM_PASSWORD = 1

        fun isValidPasswords(
            password: String,
            confirmPassword: String
        ): HashMap<Int, String?>? {
            val hashIsValid = hashMapOf<Int, String?>()
            hashIsValid[PASSWORD] = null
            hashIsValid[CONFIRM_PASSWORD] = null

            var isValidated = true
            if (password.length < 8) {
                hashIsValid[PASSWORD] = "Password should be at least 8 characters"
                isValidated = false
            } else if (confirmPassword != password) {
                hashIsValid[CONFIRM_PASSWORD] = "The two passwords should match"
                isValidated = false
            }

            Log.d("User", hashIsValid.toString())

            return if (isValidated) null else hashIsValid

        }
    }
}