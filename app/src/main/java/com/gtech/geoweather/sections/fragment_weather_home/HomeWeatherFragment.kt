package com.gtech.geoweather.sections.fragment_weather_home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng
import com.gtech.geoweather.BuildConfig
import com.gtech.geoweather.api.OpenWeatherMapService
import com.gtech.geoweather.common.*
import com.gtech.geoweather.databinding.FragmentHomeWeatherBinding
import com.gtech.geoweather.models.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeWeatherFragment : Fragment() {

    val TAG = "Home Weather"
    private val viewModel by lazy { ViewModelProvider(this)[HomeWeatherViewModel::class.java] }
    private val binding by lazy { FragmentHomeWeatherBinding.inflate(layoutInflater) }
    private lateinit var locationPermissionHandler: LocationPermissionHandler
    private val locationRequest =
        LocationRequest.Builder(LocationRequest.PRIORITY_HIGH_ACCURACY, 5000).build()
    private val openWeatherMapService by lazy { OpenWeatherMapService(requireActivity()) }
    private val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private val firestoreWeatherHistory = FirestoreWeatherHistory(firebaseAuth.currentUser?.uid)
    private val progressDialog by lazy { ProgressDialogCustom(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.btnAcceptPermission.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()

        if (viewModel.weatherUpdate.value == null) {
            //check permission and add call the api
            locationPermissionHandler =
                LocationPermissionHandler(requireActivity(), permissionRequest)
            locationPermissionHandler.checkPermission()
            progressDialog.show()
        }
    }

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // permission granted
                // get location here
                progressDialog.show()
                buildGPSDialog()
            } else if (locationPermissionHandler.permissionDeniedPreviously) {
                // permission denied again, show custom dialog again
                progressDialog.dismiss()
                locationPermissionHandler.showCustomDialog()
            } else {
                // permission denied, show message or take other action
                binding.scrollContent.visibility = View.GONE
                binding.relativeAcceptPermission.visibility = View.VISIBLE
                progressDialog.dismiss()
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

            progressDialog.dismiss()
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
                binding.scrollContent.visibility = View.VISIBLE
                binding.relativeAcceptPermission.visibility = View.GONE
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    // Handle the weather response here
                    val weatherResponse = response.body()
                    Log.d(TAG, weatherResponse.toString())
                    Log.d(TAG, weatherResponse?.getWeatherDatabaseData().toString())

                    viewModel.weatherUpdate.value = weatherResponse

                    weatherResponse?.let {
                        firestoreWeatherHistory.insert(weatherResponse.getWeatherDatabaseData())
                    }

                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                progressDialog.dismiss()
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
            binding.txtCurrentTemperature.text = temp


            //==========WEATHER=========
            val weather = weatherResponse.weather
            if (weather.isNotEmpty()) {
                val firstWeather = weather[0]
                val imageSize = resources.getDimension(com.intuit.sdp.R.dimen._50sdp)
                context?.loadImageWithGlide(firstWeather.icon, imageSize, binding.imgWeather)

                // Weather Description
                val textCapWordsDescription = firstWeather.description.split(" ")
                    .joinToString(" ") { it.replaceFirstChar { firstChar -> firstChar.uppercase() } }
                binding.txtWeatherDesc.text = textCapWordsDescription

                // Description Sentences
                val extraSentence = WeatherDescriptions.descriptions[firstWeather.id.toString()]
                binding.txtWeatherDescAdditional.text = extraSentence
            }

            //==========TIMES OF DAY===========
            // Background
            val timesOfDay = weatherResponse.dt.getTimeOfDay()
            var primaryTextColor: Int
            var secondaryTextColor: Int // for description only
            var cardBackgroundColor: Int
            val backgroundColor = when (timesOfDay) {
                timesOfDay.NIGHT -> {
                    primaryTextColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.white)
                    secondaryTextColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.white_text_color)
                    cardBackgroundColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.black_charcoal1)
                    requireContext().getContextCompatColor(com.gtech.geoweather.R.color.black_charcoal2)
                }
                timesOfDay.AFTERNOON -> {
                    primaryTextColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.black)
                    secondaryTextColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.black_charcoal2)
                    cardBackgroundColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.afternoon_card)
                    requireContext().getContextCompatColor(com.gtech.geoweather.R.color.afternoon)
                }
                else -> { //day by default
                    primaryTextColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.black)
                    secondaryTextColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.black_charcoal2)
                    cardBackgroundColor =
                        requireContext().getContextCompatColor(com.gtech.geoweather.R.color.white)
                    requireContext().getContextCompatColor(com.gtech.geoweather.R.color.white_text_color)
                }
            }
            binding.root.setBackgroundColor(backgroundColor)
            setTextColors(
                primaryTextColor,
                secondaryTextColor,
                cardBackgroundColor,
                backgroundColor
            )

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
            val sunrise = sys.sunrise.toFormattedTime()
            val sunset = sys.sunset.toFormattedTime()
            binding.txtSunrise.text = sunrise
            binding.txtSunset.text = sunset
        }

    }

    private fun setTextColors(
        primaryTextColor: Int,
        secondaryTextColor: Int,
        cardBackgroundColor: Int,
        backgroundColor: Int
    ) {
        binding.root.setBackgroundColor(backgroundColor)

        binding.txtLocation.setTextColor(primaryTextColor)
        binding.txtCurrentTemperature.setTextColor(primaryTextColor)

        binding.cardWeatherDetails.setCardBackgroundColor(cardBackgroundColor)
        binding.txtWeatherDesc.setTextColor(primaryTextColor)
        binding.txtWeatherDescAdditional.setTextColor(secondaryTextColor)

        binding.txtSunrise.setTextColor(primaryTextColor)
        binding.txtSunset.setTextColor(primaryTextColor)
    }

}