package com.gtech.geoweather.sections.activity_register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.gtech.geoweather.common.AppBarCustom
import com.gtech.geoweather.common.PasswordUtils
import com.gtech.geoweather.databinding.ActivityRegisterBinding
import com.gtech.geoweather.local_database.AppDatabase
import com.gtech.geoweather.models.User
import com.gtech.geoweather.sections.activity_landing_page.LandingPageActivity
import kotlinx.coroutines.*


class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel: RegisterViewModel by viewModels()
    private val userDao by lazy { AppDatabase.getDatabase(this).userDao() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAppBar()

        binding.btnSignUp.setOnClickListener {
            validateRegister()
        }

    }


    private fun setupAppBar() {
        val appBarCustom = AppBarCustom(this, true)
        appBarCustom.title = "Sign In"
    }

    private fun validateRegister() {
        resetErrors()
        val firstName = binding.tILayoutFirstName.editText?.text.toString()
        val middleName = binding.tILayoutMiddleName.editText?.text.toString()
        val lastName = binding.tILayoutLastName.editText?.text.toString()
        val email = binding.tILayoutEmail.editText?.text.toString()
        val mobileNumber = binding.tILayoutMobileNumber.editText?.text.toString()
        val password = binding.tILayoutPassword.editText?.text.toString()
        val confirmPassword = binding.tILayoutConfirmPassword.editText?.text.toString()
        val user = User(
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            email = email,
            mobileNumber = mobileNumber,
            hashedPassword = ""
        )

        //Validate errors first
        val errors = user.errors()
        if (errors != null) {
            d("Register", errors.toString())
            // Not Validated - display errors
            binding.tILayoutFirstName.error = errors.firstName
            binding.tILayoutMiddleName.error = errors.middleName
            binding.tILayoutLastName.error = errors.lastName
            binding.tILayoutEmail.error = errors.email
            binding.tILayoutMobileNumber.error = errors.mobileNumber
            return
        }

        //Validated - check password
        val passwordValidator = User.isValidPasswords(password, confirmPassword)
        if (passwordValidator != null) {
            d("Register", "1")
            binding.tILayoutPassword.error = passwordValidator[User.PASSWORD]
            binding.tILayoutConfirmPassword.error = passwordValidator[User.CONFIRM_PASSWORD]
            return
        }

        // Validated - register the user
        val newUser = user.copy(hashedPassword = PasswordUtils.hash(password))
        register(newUser)
    }

    private fun resetErrors() {
        for (view: View in binding.linearForm.children) {
            if (view is TextInputLayout) {
                view.error = null
            }
        }
    }

    private fun register(user: User) {
        lifecycleScope.launch {
            val foundByMobile = userDao.findByMobile(user.mobileNumber)
            if (foundByMobile != null) {
                withContext(Dispatchers.Main) {
                    binding.tILayoutMobileNumber.error = "This Mobile Number is already registered"
                }
            }
            val foundByEmail = userDao.findByEmail(user.email)
            if (foundByEmail != null) {
                withContext(Dispatchers.Main) {
                    binding.tILayoutEmail.error = "This Email is already registered"
                }
            }

            if (foundByMobile != null || foundByEmail != null) {
                // there are error/s
                return@launch
            }
            //register
            userDao.insert(user)
            //go to landing page
            val landingPageIntent = Intent(this@RegisterActivity, LandingPageActivity::class.java)
            landingPageIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(landingPageIntent)
            finish()

        }
        CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {}
    }
}