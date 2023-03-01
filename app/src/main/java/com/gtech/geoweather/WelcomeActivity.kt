package com.gtech.geoweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gtech.geoweather.databinding.ActivityWelcomeBinding
import com.gtech.geoweather.sections.activity_register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            startActivity(Intent(this, RegisterActivity::class.java))
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

}