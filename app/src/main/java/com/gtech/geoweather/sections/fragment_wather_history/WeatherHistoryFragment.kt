package com.gtech.geoweather.sections.fragment_wather_history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.WelcomeActivity
import com.gtech.geoweather.common.*
import com.gtech.geoweather.databinding.FragmentWeatherHistoryBinding
import com.gtech.geoweather.models.User
import com.gtech.geoweather.models.WeatherDatabaseData
import com.gtech.geoweather.sections.activity_register.RegisterActivity
import java.text.SimpleDateFormat
import java.util.*

class WeatherHistoryFragment : Fragment(),
    com.google.firebase.firestore.EventListener<DocumentSnapshot> {


    private val TAG = "WeatherHistory"

    private val viewModel by lazy {
        ViewModelProvider(this)[WeatherHistoryViewModel::class.java]
    }
    private val binding by lazy {
        FragmentWeatherHistoryBinding.inflate(layoutInflater)
    }
    private val progressDialog by lazy { ProgressDialogCustom(requireActivity()) }

    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val firestoreWeatherHistory = FirestoreWeatherHistory(firebaseAuth.currentUser?.uid)
    private val firestoreUser = FirestoreUser(firebaseAuth.currentUser?.uid)
    private val simpleAlertDialog by lazy {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setCancelable(false)
        dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtLogout.setOnClickListener {
            logoutUser()
        }
        binding.txtEditProfile.setOnClickListener {
            val registerIntent = Intent(requireContext(), RegisterActivity::class.java)
            //Put Edit
            registerIntent.putExtra(REGISTER_INTENTION, IS_EDIT)
            startActivity(registerIntent)
        }

        //Retrieve data realtime
        updatePageData()
    }

    //Retrieve data realtime
    private fun updatePageData() {
        progressDialog.show()
        firestoreUser.firebaseCollection.document(firebaseAuth.currentUser?.uid ?: "")
            .addSnapshotListener(this)

        firestoreWeatherHistory.firebaseCollection.addSnapshotListener(weatherHistoryListener)
    }

    private fun observe() {
        viewModel.weatherHistoryData.observe(viewLifecycleOwner) { weatherDatabaseData ->
            if (weatherDatabaseData == null) {
                binding.recyclerHistory.layoutManager = null
            } else {
                binding.recyclerHistory.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                val weatherHistoryAdapter =
                    WeatherHistoryAdapter(requireActivity(), weatherDatabaseData)
                binding.recyclerHistory.adapter = weatherHistoryAdapter
            }
            //set adapter
        }
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                val nameList =
                    listOf(it.firstName.trim(), it.middleName.trim(), it.lastName.trim())
                val fullName = nameList.joinToString(" ")
                binding.txtFullName.text = fullName
                binding.eTxtEmail.text = it.email
            }
        }
    }

    private fun showLogoutDialog(title: String, message: String) {
        simpleAlertDialog.setTitle(title)
        simpleAlertDialog.setMessage(message)
        simpleAlertDialog.show()
    }

    override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {

        if (error != null) {
            showErrorDialog(error)
            return
        }
        if (value != null && value.exists()) {
            updateUserData(value)
        }
    }

    private fun showErrorDialog(error: FirebaseFirestoreException) {
        // handle the exception
        simpleAlertDialog.setPositiveButton("Ok") { mDialog, _ ->
            firebaseAuth.signOut()
            mDialog.dismiss()
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            activity?.finish()
        }
        simpleAlertDialog.setNegativeButton(null, null)
        showLogoutDialog("Error", error.message ?: "Something went wrong")
    }

    private val weatherHistoryListener =
        EventListener<QuerySnapshot> { value, error ->

            progressDialog.dismiss()
            if (error != null) {
                showErrorDialog(error)
                return@EventListener
            }
            if (value != null) {
                updateRecyclerView(value)
            }
        }

    private fun updateRecyclerView(value: QuerySnapshot) {

        val documents = value.documents.map {
            WeatherDatabaseData(
                it.get("temperature", Double::class.java) ?: 0.0,
                it.get("tem_min", Double::class.java) ?: 0.0,
                it.get("temp_max", Double::class.java) ?: 0.0,
                it.getString("description") ?: "",
                it.getString("icon") ?: "",
                it.get("windSpeed", Double::class.java) ?: 0.0,
                it.get("timezone", Int::class.java) ?: 0,
                it.getString("country") ?: "",
                it.getString("cityName") ?: "",
                it.get("sunrise", Int::class.java) ?: 0,
                it.get("sunset", Int::class.java) ?: 0,
                it.get("dateTime", Int::class.java) ?: 0,
            )
        }

        val weatherMap = documents.groupBy {
            val date = Date(it.dateTime.toLong().times(1000))
            SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date)
        }
        viewModel.weatherHistoryData.value = weatherMap
    }

    private fun updateUserData(value: DocumentSnapshot) {
        val user = User(
            value.get("id", Int::class.java) ?: 0,
            value.getString("firstName") ?: "",
            value.getString("middleName") ?: "",
            value.getString("lastName") ?: "",
            value.getString("email") ?: "",
            value.getString("mobileNumber") ?: "",
        )
        viewModel.user.value = user
    }

    private fun logoutUser() {
        simpleAlertDialog.setPositiveButton("Yes") { mDialog, _ ->
            firebaseAuth.signOut()
            mDialog.dismiss()
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            activity?.finish()
        }
        simpleAlertDialog.setNegativeButton("No") { mDialog, _ ->
            mDialog.dismiss()
        }
        showLogoutDialog("Logout", "Are you sure that you want to logout?")
    }

}