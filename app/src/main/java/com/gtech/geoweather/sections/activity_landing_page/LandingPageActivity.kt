package com.gtech.geoweather.sections.activity_landing_page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.gtech.geoweather.databinding.ActivityLandingPageBinding
import com.gtech.geoweather.sections.fragment_wather_history.WeatherHistoryFragment
import com.gtech.geoweather.sections.fragment_weather_home.HomeWeatherFragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class LandingPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLandingPageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            val fragments = listOf(HomeWeatherFragment(), WeatherHistoryFragment())
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                replaceFragments(fragments[newIndex])
            }

        })

        binding.bottomBar.selectTabAt(0)

    }

    private fun replaceFragments(selected: Fragment) {

        // Insert the fragment by replacing any existing fragment

        supportFragmentManager.beginTransaction().apply {
            //hide all fragments
            supportFragmentManager.fragments.forEach {
                hide(it)
            }
            val fragmentCustomTag = selected.tag ?: selected.javaClass.simpleName
            Log.d("Landing", fragmentCustomTag)
            val foundFragment =
                supportFragmentManager.findFragmentByTag(fragmentCustomTag)
            if (foundFragment != null) {
                //show the selected.
                show(foundFragment)
            } else {
                //add the selected
                add(
                    binding.frameLayout.id,
                    selected,
                    fragmentCustomTag
                )
            }
            commit()
        }
    }
}