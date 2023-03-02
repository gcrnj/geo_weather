package com.gtech.geoweather.sections.fragment_weather_home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.type.LatLng
import com.gtech.geoweather.BuildConfig
import com.gtech.geoweather.api.OpenWeatherMapService
import com.gtech.geoweather.common.LocationPermissionHandler
import com.gtech.geoweather.databinding.FragmentHomeWeatherBinding
import com.gtech.geoweather.models.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileInputStream
import java.util.*

class HomeWeatherFragment : Fragment() {

    val TAG = "Home Weather"
    private val viewModel by lazy {
        ViewModelProvider(this)[HomeWeatherViewModel::class.java]
    }

    private val binding by lazy {
        FragmentHomeWeatherBinding.inflate(layoutInflater)
    }
    private lateinit var locationPermissionHandler: LocationPermissionHandler

    val locationRequest =
        LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 5000).build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//check permission and add call the api
        locationPermissionHandler = LocationPermissionHandler(requireActivity(), permissionRequest)
        locationPermissionHandler.checkPermission()

    }

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // permission granted
                // get location here
                Toast.makeText(activity, "Permission Granted!", Toast.LENGTH_SHORT).show()
                buildGPSDialog()
            } else if (locationPermissionHandler.permissionDeniedPreviously) {
                // permission denied again, show custom dialog again
                Toast.makeText(activity, "Permission is needed.", Toast.LENGTH_SHORT).show()
                locationPermissionHandler.showCustomDialog()
            } else {
                // permission denied, show message or take other action
                Toast.makeText(activity, "Permission Denied.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun buildGPSDialog() {
// Create a LocationSettingsRequest.Builder object
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

// Check if the current location settings satisfy the LocationSettingsRequest
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Location settings satisfied, continue with location request
            getLocationLatLng()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // Location settings not satisfied, prompt user to update settings
                    gpsRequest.launch(
                        IntentSenderRequest.Builder(exception.resolution.intentSender).build()
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            } else {
                // Location settings not satisfied, but cannot prompt user to update settings
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("Location Required")
                dialogBuilder.setMessage("To use this feature, please turn on location services for the app.")
                dialogBuilder.setPositiveButton("Go to Settings") { dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                dialogBuilder.setNegativeButton("No Thanks") { dialog, which ->
                    // Handle "No Thanks" button click
                    dialog.dismiss()
                }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        }
    }

    private val gpsRequest =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    // The user turned on the required location settings
                    getLocationLatLng()
                }
                Activity.RESULT_CANCELED -> {
                    // The user clicked the "Cancel" button in the dialog
                }
            }
        }

    @SuppressLint("MissingPermission")
    fun getLocationLatLng() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Request the current location callback
        val locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {

                val location = locationResult.lastLocation
                location ?: return // Return if locationResult is null

                val lat = location.latitude
                val lng = location.longitude
                // Do something with the lat and lng
                Log.d(TAG, "Location = $lat,$lng")

                // Remove the location updates when you are done
                fusedLocationClient.removeLocationUpdates(this)

                // Get Weather Data
                val builder = LatLng.newBuilder()
                builder.latitude = location.latitude
                builder.longitude = location.longitude
                val latLng = builder.build()
                getWeatherData(latLng)
            }
        }

        // Request a single location update
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val openWeatherMapService by lazy {
        OpenWeatherMapService(requireActivity())
    }

    fun getWeatherData(latLng: LatLng) {
        val weatherApi = openWeatherMapService.api

        val apiKey = BuildConfig.WEATHER_API_KEY
        val currentWeatherRequest = weatherApi.getCurrentWeather(
            latLng.latitude,
            latLng.longitude,
            apiKey
        )

        currentWeatherRequest.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                Log.d(TAG, response.toString())
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    // Handle the weather response here
                } else {
                    // Handle the error here
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e(TAG, "Current Weather Request Error", t)
            }

        })
    }
}