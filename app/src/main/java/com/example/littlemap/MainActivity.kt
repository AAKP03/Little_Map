package com.example.littlemap

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private var startLocation: Location? = null
    private var endLocation: Location? = null

    private lateinit var btnStart: Button
    private lateinit var btnEnd: Button
    private lateinit var tvStart: TextView
    private lateinit var tvEnd: TextView
    private lateinit var tvDistance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnStart = findViewById(R.id.btnStart)
        btnEnd = findViewById(R.id.btnEnd)
        tvStart = findViewById(R.id.tvStart)
        tvEnd = findViewById(R.id.tvEnd)
        tvDistance = findViewById(R.id.tvDistance)

        btnStart.setOnClickListener {
            if (hasLocationPermission()) fetchStartLocation() else requestLocationPermission()
        }

        btnEnd.setOnClickListener {
            if (hasLocationPermission()) fetchEndLocation() else requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchStartLocation() {
        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        try {
            fusedLocationClient.getCurrentLocation(request, null)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        startLocation = location
                        tvStart.text = "Start Location:\nLatitude: %.5f\nLongitude: %.5f"
                            .format(location.latitude, location.longitude)
                        btnEnd.isEnabled = true
                    } else {
                        Toast.makeText(this, "Couldn't get a location fix. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to get start location", Toast.LENGTH_SHORT).show()
                }
        } catch (_: SecurityException) {
            Toast.makeText(this, "Location permission was revoked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchEndLocation() {
        val start = startLocation
        if (start == null) {
            Toast.makeText(this, "Set start point first", Toast.LENGTH_SHORT).show()
            return
        }

        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        try {
            fusedLocationClient.getCurrentLocation(request, null)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        endLocation = location
                        tvEnd.text = "End Location:\nLatitude: %.5f\nLongitude: %.5f"
                            .format(location.latitude, location.longitude)

                        val distanceMeters = start.distanceTo(location)
                        tvDistance.text = if (distanceMeters > 1000) {
                            "%.2f km".format(distanceMeters / 1000)
                        } else {
                            "%.2f meters".format(distanceMeters)
                        }
                    } else {
                        Toast.makeText(this, "Couldn't get a location fix. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to get end location", Toast.LENGTH_SHORT).show()
                }
        } catch (_: SecurityException) {
            Toast.makeText(this, "Location permission was revoked", Toast.LENGTH_SHORT).show()
        }
    }
}