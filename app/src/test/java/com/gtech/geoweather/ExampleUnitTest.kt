package com.gtech.geoweather

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
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
}