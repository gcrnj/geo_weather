package com.gtech.geoweather

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private lateinit var scene : ActivityScenario<WelcomeActivity>

    @Before
    fun initiate(){
        scene = launchActivity()
        scene.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testRegister(){
        val firstName = "Gio"
        val middleName = "Borgonia"
        val lastName = "Cornejo"
        val email = "giocornejo777@gmail.com"
        val mobileNumber = "9102135313"
        val password = "123456789"
        val confirmPassword = "12345678"
    }
}