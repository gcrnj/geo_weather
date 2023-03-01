package com.gtech.geoweather.sections.activity_sign_in

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gtech.geoweather.common.AppBarCustom
import com.gtech.geoweather.databinding.ActivitySignInBinding
import com.gtech.geoweather.models.User

class SignInActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}