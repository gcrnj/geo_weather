package com.gtech.geoweather.sections.activity_landing_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.WelcomeActivity
import com.gtech.geoweather.databinding.ActivityLandingPageBinding
import nl.joery.animatedbottombar.AnimatedBottomBar

class LandingPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLandingPageBinding.inflate(layoutInflater) }

    private val viewModel by lazy {
        ViewModelProvider(this)[LandingPageViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Firebase.auth.currentUser == null) {
            logoutUser()
            return
        }
        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                replaceFragments(viewModel.fragments.value!![newIndex])
                viewModel.currentTabSelected.value = newIndex
            }

        })

        observe()
    }

    private fun observe() {
        viewModel.currentTabSelected.observe(this) {
            if (it != binding.bottomBar.selectedIndex)
                binding.bottomBar.selectTabAt(it)
        }
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

    private fun logoutUser() {
        Firebase.auth.signOut()
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}