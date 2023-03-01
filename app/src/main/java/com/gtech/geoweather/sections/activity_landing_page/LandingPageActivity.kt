package com.gtech.geoweather.sections.activity_landing_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gtech.geoweather.R
import com.gtech.geoweather.databinding.ActivityLandingPageBinding

class LandingPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLandingPageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}