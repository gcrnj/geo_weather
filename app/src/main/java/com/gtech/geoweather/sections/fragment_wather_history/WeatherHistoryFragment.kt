package com.gtech.geoweather.sections.fragment_wather_history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gtech.geoweather.R
import com.gtech.geoweather.databinding.FragmentWeatherHistoryBinding

class WeatherHistoryFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this)[WeatherHistoryViewModel::class.java]
    }
    private val binding by lazy {
        FragmentWeatherHistoryBinding.inflate(layoutInflater)
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