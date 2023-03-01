package com.gtech.geoweather.common

import android.app.Activity

fun Activity.setupAppBar(title: String, hasBackButton: Boolean) {
    val appBarCustom = AppBarCustom(this, hasBackButton)
    appBarCustom.title = title
}