package com.gtech.geoweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.databinding.ActivityWelcomeBinding
import com.gtech.geoweather.sections.activity_landing_page.LandingPageActivity
import com.gtech.geoweather.sections.activity_register.RegisterActivity
import com.gtech.geoweather.sections.activity_sign_in.SignInActivity

class WelcomeActivity : AppCompatActivity() {


    private val firebaseAuth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        checkFirebaseAuth()

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun checkFirebaseAuth() {
        val currentFirebaseUser = firebaseAuth.currentUser ?: return

        currentFirebaseUser.email
        //there is a user already
        // go to landing page
        val landingPageIntent = Intent(this, LandingPageActivity::class.java)
        landingPageIntent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(landingPageIntent)
        finish()
    }

}