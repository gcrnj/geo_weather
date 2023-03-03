package com.gtech.geoweather.sections.activity_register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.common.*
import com.gtech.geoweather.databinding.ActivityRegisterBinding
import com.gtech.geoweather.models.User
import com.gtech.geoweather.sections.activity_landing_page.LandingPageActivity
import kotlinx.coroutines.*


class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val firestoreUser = FirestoreUser(firebaseAuth.currentUser?.uid)
    private val isEdit by lazy { intent.getIntExtra(REGISTER_INTENTION, 0) == IS_EDIT }
    private val simpleAlertDialog by lazy {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { mDialog, _ ->
            mDialog.dismiss()
        }
        dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val title =
            if (isEdit) {
                binding.eTxtEmail.isEnabled = false
                setFieldsUserData()
                "Edit Profile"
            } else {
                binding.eTxtEmail.isEnabled = true
                "Register"
            }


        setupAppBar(title, true)

        binding.btnSignUp.setOnClickListener {
            validateData()
        }

    }

    private fun setFieldsUserData() {

        lifecycleScope.launch {
            val userData = firestoreUser.get()
            userData?.let {
                binding.eTxtFirstName.setText(it.firstName)
                binding.eTxtMiddleName.setText(it.middleName)
                binding.eTxtLastName.setText(it.lastName)
                binding.eTxtMobileNumber.setText(it.mobileNumber)
                binding.eTxtEmail.setText(it.email)
            }

        }
    }


    private fun validateData() {
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
        )

        //Validate errors first
        val errors = user.errors()
        if (errors != null) {
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
            binding.tILayoutPassword.error = passwordValidator[User.PASSWORD]
            binding.tILayoutConfirmPassword.error = passwordValidator[User.CONFIRM_PASSWORD]
            return
        }

        // Validated - check email if already registered
        createUserInAuth(user, password)
    }

    //Creates
    private fun createUserInAuth(newUser: User, password: String) {
        val onCompleteListener = OnCompleteListener<AuthResult> { task ->
            if (task.isSuccessful) {
                //User registered successfully
                registerInDb(newUser)
            } else {
                // User not registered
                task.exception?.printStackTrace()
                val errorMessage = task.exception?.message ?: "Message not found"
                if (errorMessage.contains("email") && !isEdit)
                    binding.tILayoutEmail.error = errorMessage
                else
                    showAlertDialog(
                        "Failed to create user",
                        errorMessage
                    )
            }
        }
        if (isEdit) //Edit
            firebaseAuth.signInWithEmailAndPassword(newUser.email, password)
                .addOnCompleteListener(onCompleteListener)
        else //Create
            firebaseAuth.createUserWithEmailAndPassword(newUser.email, password)
                .addOnCompleteListener(onCompleteListener)
    }

    private fun registerInDb(newUser: User) {
        firestoreUser.insert(newUser)
            .addOnSuccessListener {
                //authenticate in firebase firestore
                updateActivity()
            }
            .addOnFailureListener { exception ->
                //failed to create the user
                showAlertDialog(
                    "Failed to create user",
                    exception.message ?: "Message not found"
                )
                //since it is already a logged in account, then I need to logout that account
                firebaseAuth.signOut()
            }
    }

    private fun resetErrors() {
        for (view: View in binding.linearForm.children) {
            if (view is TextInputLayout) {
                view.error = null
            }
        }
    }

    private fun updateActivity() {
        if (!isEdit) {
            //go to landing page
            val landingPageIntent =
                Intent(this@RegisterActivity, LandingPageActivity::class.java)
            landingPageIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(landingPageIntent)
        }
        finish()

    }

    fun showAlertDialog(title: String, message: String) {
        simpleAlertDialog.setTitle(title)
        simpleAlertDialog.setMessage(message)
        simpleAlertDialog.show()
    }
}