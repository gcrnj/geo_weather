package com.gtech.geoweather

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UserTesting {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }


    private lateinit var welcomeScene: ActivityScenario<WelcomeActivity>
    private lateinit var registerScene: ActivityScenario<WelcomeActivity>


    @Before
    fun initiate() {
        welcomeScene = launchActivity()
        welcomeScene.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun testRegister() {
        click(R.id.btnSignUp)
        var firstName = ""
        var middleName = ""
        var lastName = ""
        var email = ""
        var mobileNumber = ""
        var password = ""
        var confirmPassword = ""
        // Type registration data into the EditTexts
        type(R.id.eTxtFirstName, firstName)
        type(R.id.eTxtMiddleName, middleName)
        type(R.id.eTxtLastName, lastName)
        type(R.id.eTxtEmail, email)
        type(R.id.eTxtMobileNumber, mobileNumber)
        type(R.id.eTxtPassword, password)
        type(R.id.eTxtConfirmPassword, confirmPassword)
        // Click the register button
        scrollClick(R.id.btnSignUp)

        firstName = "Gio"
        middleName = "Borgonia"
        lastName = "Cornejo"
        email = "giocornejo777@gmail.com"
        mobileNumber = "9102135311"
        password = "123456789"
        confirmPassword = "12345678"
        // Type registration data into the EditTexts
        type(R.id.eTxtFirstName, firstName)
        type(R.id.eTxtMiddleName, middleName)
        type(R.id.eTxtLastName, lastName)
        type(R.id.eTxtEmail, email)
        type(R.id.eTxtMobileNumber, mobileNumber)
        type(R.id.eTxtPassword, password)
        type(R.id.eTxtConfirmPassword, confirmPassword)
        // Click the register button
        scrollClick(R.id.btnSignUp)
    }

    private fun type(editTextId: Int, stringToBeTyped: String) {
        onView(withId(editTextId)).perform(scrollTo(), typeText(stringToBeTyped))
    }

    private fun click(viewId: Int) {
        onView(withId(viewId)).perform(click())
    }

    private fun scrollClick(viewId: Int) {
        onView(withId(viewId)).perform(scrollTo(), click())
    }
}