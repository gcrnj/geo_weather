package com.gtech.geoweather.sections.activity_register

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.common.setupAppBar
import com.gtech.geoweather.databinding.ActivityRegisterBinding
import com.gtech.geoweather.local_database.AppDatabase
import com.gtech.geoweather.models.User
import com.gtech.geoweather.sections.activity_landing_page.LandingPageActivity
import kotlinx.coroutines.*


class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val userDao by lazy { AppDatabase.getDatabase(this).userDao() }
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val firestoreDb by lazy { Firebase.firestore }
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

        setupAppBar("Register", true)

        binding.btnSignUp.setOnClickListener {
            validateRegister()
        }

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

        // Validated - check email if already registered
        createUserInAuth(user, password)
//        registerToLocalDB(user)
    }

    private fun createUserInAuth(newUser: User, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(newUser.email, password)
            .addOnCompleteListener(this,
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //User registered successfully
                        registerInDb(newUser)
                    } else {
                        // User not registered
                        task.exception?.printStackTrace()
                        val errorMessage = task.exception?.message ?: "Message not found"
                        if (errorMessage.contains("email"))
                            binding.tILayoutEmail.error = errorMessage
                        else
                            showAlertDialog(
                                "Failed to create user",
                                errorMessage
                            )
                    }
                })
    }

    private fun registerInDb(newUser: User) {
        firestoreDb.collection("users").add(newUser)
            .addOnSuccessListener {
                //authenticate in firebase firestore
                registerToLocalDB(newUser)
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

    private fun registerToLocalDB(user: User) {
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

    fun showAlertDialog(title: String, message: String) {
        simpleAlertDialog.setTitle(title)
        simpleAlertDialog.setMessage(message)
        simpleAlertDialog.show()
    }
}