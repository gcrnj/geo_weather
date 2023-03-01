package com.gtech.geoweather.sections.activity_sign_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.gtech.geoweather.common.setupAppBar
import com.gtech.geoweather.databinding.ActivitySignInBinding
import com.gtech.geoweather.local_database.AppDatabase
import com.gtech.geoweather.sections.activity_landing_page.LandingPageActivity

class SignInActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(binding.root)

        setupAppBar("Sign In", true)

        binding.btnSignIn.setOnClickListener {
            validateLogin()
        }

    }

    private fun validateLogin() {
        val email = binding.tILayoutEmail.editText?.text.toString()
        val password = binding.tILayoutPassword.editText?.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success, do something
                    val currentFirebaseUser = firebaseAuth.currentUser
                    if (currentFirebaseUser == null) {
                        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_LONG)
                            .show()
                        return@addOnCompleteListener
                    }

                    // go to landing page
                    val landingPageIntent = Intent(this, LandingPageActivity::class.java)
                    landingPageIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(landingPageIntent)
                    finish()
                } else {
                    // Login failed, display an error message
                    binding.tILayoutPassword.error =
                        "Account not found. Please check your credentials."
                }
            }
    }
}