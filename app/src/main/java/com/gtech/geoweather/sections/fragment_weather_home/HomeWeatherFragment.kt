package com.gtech.geoweather.sections.fragment_weather_home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gtech.geoweather.databinding.FragmentHomeWeatherBinding

class HomeWeatherFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeWeatherViewModel::class.java]
    }

    private val binding by lazy {
        FragmentHomeWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}