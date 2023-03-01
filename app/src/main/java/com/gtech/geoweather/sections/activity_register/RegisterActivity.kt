package com.gtech.geoweather.sections.activity_register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.gtech.geoweather.common.AppBarCustom
import com.gtech.geoweather.databinding.ActivityRegisterBinding
import com.gtech.geoweather.models.User

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

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
        val firstName = binding.tILayoutFirstName.editText?.text.toString()
        val middleName = binding.tILayoutMiddleName.editText?.text.toString()
        val lastName = binding.tILayoutLastName.editText?.text.toString()
        val email = binding.tILayoutEmail.editText?.text.toString()
        val mobileNumber = binding.tILayoutMobileNumber.editText?.text.toString()
        val user = User(firstName, middleName, lastName, email, mobileNumber)

        //Validate errors first
        user.errors?.let {
            d("Regiter", it.toString())
            // Not Validated - display errors
            binding.tILayoutFirstName.error = it.firstName
            binding.tILayoutMiddleName.error = it.middleName
            binding.tILayoutLastName.error = it.lastName
            binding.tILayoutEmail.error = it.email
            binding.tILayoutMobileNumber.error = it.mobileNumber
        }?.run {

            //Validated - check password
            val password = binding.tILayoutPassword.editText?.text.toString()
            val confirmPassword = binding.tILayoutConfirmPassword.editText?.text.toString()

            val passwordValidator = User.isValidPasswords(password, confirmPassword)
            passwordValidator?.let {
                binding.tILayoutPassword.error = it[User.PASSWORD]
                binding.tILayoutConfirmPassword.error = it[User.CONFIRM_PASSWORD]
            }?.run {
                // Validated - TODO register the user

            }
        }
    }
}