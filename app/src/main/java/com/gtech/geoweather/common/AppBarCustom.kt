package com.gtech.geoweather.common

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.gtech.geoweather.R

class AppBarCustom(
    private val activity: Activity,
    hasBackButton: Boolean
) {

    private val txtTitle: TextView = activity.findViewById(R.id.txtTitle)
    val btnBack: View =
        activity.findViewById(R.id.btnBack)

    var title: String? = ""
        set(value) {
            field = value
            txtTitle.text = value
        }

    init {
        txtTitle.text = title


        //Show button and click listener by default
        if (hasBackButton) {
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                activity.finish()
            }
        } else {
            btnBack.visibility = View.GONE
        }
    }
}