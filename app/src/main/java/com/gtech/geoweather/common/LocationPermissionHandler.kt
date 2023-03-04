package com.gtech.geoweather.common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gtech.geoweather.R

class LocationPermissionHandler(
    private val activity: Activity,
    private val permissionRequest: ActivityResultLauncher<String>
) {

    private val tryAgainDialog by lazy {
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("Location Permission Required")
        dialog.setMessage(activity.getString(R.string.we_need_your_location_to_provide_you_with_relevant_information))
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { mDialog, _ ->
            permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            mDialog.dismiss()
        }
        dialog.setNegativeButton(null, null)
        dialog.create()
    }


    val permissionDeniedPreviously: Boolean
        get() = ActivityCompat.shouldShowRequestPermissionRationale(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        )

    private val permissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    fun checkPermission() {
        permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    fun showCustomDialog() {
        tryAgainDialog.show()
    }


}
