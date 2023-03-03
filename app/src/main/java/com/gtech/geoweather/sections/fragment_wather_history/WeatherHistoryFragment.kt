package com.gtech.geoweather.sections.fragment_wather_history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.common.FirestoreWeatherHistory
import com.gtech.geoweather.databinding.FragmentWeatherHistoryBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        observe()
        return binding.root
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
}