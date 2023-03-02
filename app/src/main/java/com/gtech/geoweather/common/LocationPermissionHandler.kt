package com.gtech.geoweather.common

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationPermissionHandler(
    private val activity: Activity,
    private val permissionRequest: ActivityResultLauncher<String>
) {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    private val tryAgainDialog by lazy {
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("Location Permission Required")
        dialog.setMessage("We need your location to provide you with relevant information.")
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { mDialog, _ ->
            permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            mDialog.dismiss()
        }
        dialog.setNegativeButton("Cancel") { mDialog, _ ->
            mDialog.dismiss()
        }
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
        if (permissionGranted) {
            // permission already granted
            // get location here
            permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else if (permissionDeniedPreviously) {
            // permission denied previously, show custom dialog
            showCustomDialog()
        } else {
            // permission not granted, request permission
            permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    fun showCustomDialog() {
        tryAgainDialog.show()
    }


}
