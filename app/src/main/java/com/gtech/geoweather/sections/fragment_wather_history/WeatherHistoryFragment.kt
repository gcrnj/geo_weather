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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.WelcomeActivity
import com.gtech.geoweather.common.FirestoreUser
import com.gtech.geoweather.common.FirestoreWeatherHistory
import com.gtech.geoweather.common.IS_EDIT
import com.gtech.geoweather.common.REGISTER_INTENTION
import com.gtech.geoweather.databinding.FragmentWeatherHistoryBinding
import com.gtech.geoweather.sections.activity_register.RegisterActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherHistoryFragment : Fragment() {

    private val TAG = "WeatherHistory"

    private val viewModel by lazy {
        ViewModelProvider(this)[WeatherHistoryViewModel::class.java]
    }
    private val binding by lazy {
        FragmentWeatherHistoryBinding.inflate(layoutInflater)
    }
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val firestoreWeatherHistory = FirestoreWeatherHistory(firebaseAuth.currentUser?.uid)
    private val firestoreUser = FirestoreUser(firebaseAuth.currentUser?.uid)
    private val simpleAlertDialog by lazy {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setCancelable(false)
        dialog.setPositiveButton("Yes") { mDialog, _ ->
            firebaseAuth.signOut()
            mDialog.dismiss()
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            activity?.finish()
        }
        dialog.setNegativeButton("No") { mDialog, _ ->
            mDialog.dismiss()
        }
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
    }

    private fun logoutUser() {
        showAlertDialog("Logout", "Are you sure that you want to logout?")
    }

    override fun onResume() {
        super.onResume()
        //Retrieve data whenever the user open/reopened to this fragment
        getHistoryData()
    }

    private fun getHistoryData() {
        lifecycleScope.launch {
            val weatherDocs = firestoreWeatherHistory.get()

            val weatherMap = weatherDocs?.groupBy {
                val date = Date(it?.dateTime?.toLong()?.times(1000) ?: 0)
                SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(date)
            }

            Log.d(TAG, weatherDocs.toString())
            viewModel.weatherHistoryData.value = weatherMap
            updateUserData()

        }
    }

    suspend fun updateUserData() {
        val userData = firestoreUser.get()
        userData?.let {
            val nameList =
                listOf(it.firstName.trim(), it.middleName.trim(), it.lastName.trim())
            val fullName = nameList.joinToString(" ")
            binding.txtFullName.text = fullName
            binding.eTxtEmail.text = it.email
        }
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
    }

    fun showAlertDialog(title: String, message: String) {
        simpleAlertDialog.setTitle(title)
        simpleAlertDialog.setMessage(message)
        simpleAlertDialog.show()
    }
}