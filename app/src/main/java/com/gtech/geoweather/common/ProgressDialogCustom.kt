package com.gtech.geoweather.common

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.gtech.geoweather.R
import com.gtech.geoweather.databinding.LayoutProgressDialogCustomBinding


class ProgressDialogCustom(activity: Activity) : Dialog(activity) {

    val layoutBinding by lazy { LayoutProgressDialogCustomBinding.inflate(activity.layoutInflater) }
    val slideAnimation: Animation = AnimationUtils.loadAnimation(activity, R.anim.slide_animation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setContentView(layoutBinding.root)
    }

    override fun show() {
        layoutBinding.linearProgressWeathers.startAnimation(slideAnimation)

        super.show()

    }
}