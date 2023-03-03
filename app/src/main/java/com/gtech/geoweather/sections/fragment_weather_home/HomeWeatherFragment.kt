package com.gtech.geoweather.sections.fragment_weather_home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.type.LatLng
import com.gtech.geoweather.BuildConfig
import com.gtech.geoweather.api.OpenWeatherMapService
import com.gtech.geoweather.common.Countries
import com.gtech.geoweather.common.LocationPermissionHandler
import com.gtech.geoweather.databinding.FragmentHomeWeatherBinding
import com.gtech.geoweather.models.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
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

    private val locationRequest =
        LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 5000).build()

    private val openWeatherMapService by lazy {
        OpenWeatherMapService(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()

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
            // Location settings is on
            getLocationLatLng()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    // Location settings off, show dialog
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

    fun getWeatherData(latLng: LatLng) {
        val weatherApi = openWeatherMapService.api

        val apiKey = BuildConfig.WEATHER_API_KEY
        val currentWeatherRequest = weatherApi.getCurrentWeather(
            latLng.latitude,
            latLng.longitude,
            "metric",
            apiKey
        )

        currentWeatherRequest.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                Log.d(TAG, response.toString())
                if (response.isSuccessful) {
                    viewModel.weatherUpdate.value = response.body()
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

    private fun observe() {
        viewModel.weatherUpdate.observe(viewLifecycleOwner) { weatherResponse ->
            //Temperature in Celsius
            val temp = getString(
                com.gtech.geoweather.R.string.degree_celsius,
                weatherResponse.main.temp.toString()
            )
            binding.txtDegreeCelsius.text = temp


            //==========WEATHER=========
            val weather = weatherResponse.weather
            if (weather.isNotEmpty()) {
                val firstWeather = weather[0]
                val weatherImageUrl =
                    "https://openweathermap.org/img/wn/${firstWeather.icon}@2x.png"
//                Glide.with(requireContext()).load(weatherImageUrl)
//                    .into(binding.imgWeather)
                val imageSize = resources.getDimension(com.intuit.sdp.R.dimen._50sdp)
                Glide.with(requireContext())
                    .load(weatherImageUrl)
                    .placeholder(com.gtech.geoweather.R.drawable.loading)
                    .into(object :
                        CustomTarget<Drawable?>(imageSize.toInt(), imageSize.toInt()) {

                        fun setDrawable(drawable: Drawable?) {
                            binding.imgWeather.setImageDrawable(drawable)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            setDrawable(placeholder)
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable?>?
                        ) {
                            setDrawable(resource)
                        }
                    })

                // Weather Description
                val textCapWordsDescription = firstWeather.description.split(" ")
                    .joinToString(" ") { it.replaceFirstChar { firstChar -> firstChar.uppercase() } }
                binding.txtWeatherDesc.text = textCapWordsDescription

            }


            //==========SYS==============
            val sys = weatherResponse.sys
            // Location
            val cityName = weatherResponse.name
            val countryName = Countries.countries[sys.country]
            binding.txtLocation.text = getString(
                com.gtech.geoweather.R.string.location_city_country,
                cityName,
                countryName
            )

            // Sunrise and Sunset
            val sunrise = convertTimeMillisToTime(sys.sunrise)
            val sunset = convertTimeMillisToTime(sys.sunset)
            binding.txtSunrise.text = sunrise
            binding.txtSunset.text = sunset
        }

    }

    fun convertTimeMillisToTime(timeInMillis: Int): String {
        val dateFormat = SimpleDateFormat("mm dd, yy hh:mm a", Locale.ENGLISH)
        Log.d("HomeWeather", dateFormat.format(Date(timeInMillis.toLong())))
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return timeFormat.format(Date(timeInMillis.toLong()))
    }

}