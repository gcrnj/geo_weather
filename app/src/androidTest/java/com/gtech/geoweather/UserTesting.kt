package com.gtech.geoweather

import android.app.Instrumentation
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.gtech.geoweather.sections.activity_landing_page.LandingPageActivity
import com.gtech.geoweather.sections.activity_register.RegisterActivity
import org.junit.Assert
import org.junit.Assert.assertTrue
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

    private val TIME_OUT = 5000L /* miliseconds */

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

        firstName = TestConstants.R_FIRST_NAME
        middleName = TestConstants.R_MIDDLE_NAME
        lastName = TestConstants.R_LAST_NAME
        email = TestConstants.R_EMAIL
        mobileNumber = TestConstants.R_MOBILE_NUMBER
        password = TestConstants.R_PASSWORD
        confirmPassword = TestConstants.R_CONFIRM_PASSWORD

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


        assertTrue(welcomeScene.state == Lifecycle.State.DESTROYED)

    }

    fun testLogout(){
        click()
    }

    @Test
    fun testLogin() {
        click(R.id.btnSignUp)
        var email = ""
        var password = ""
        // Type registration data into the EditTexts
        type(R.id.eTxtEmail, email)
        type(R.id.eTxtPassword, password)
        // Click the register button
        scrollClick(R.id.btnSignUp)

        email = TestConstants.LOGIN_EMAIL
        password = TestConstants.LOGIN_PASSWORD
        // Type registration data into the EditTexts
        type(R.id.eTxtEmail, email)
        type(R.id.eTxtPassword, password)
        // Click the register button
        scrollClick(R.id.btnSignUp)

        assertTrue(welcomeScene.state == Lifecycle.State.DESTROYED)
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