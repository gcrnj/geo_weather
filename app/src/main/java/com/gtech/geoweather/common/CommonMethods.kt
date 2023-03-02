package com.gtech.geoweather.common

import android.app.Activity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService

fun Activity.setupAppBar(title: String, hasBackButton: Boolean) {
    val appBarCustom = AppBarCustom(this, hasBackButton)
    appBarCustom.title = title
}

fun Activity.hideSoftKeyboard() {
    val inputMethodManager: InputMethodManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isActive) {
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

//Show keyboard
fun EditText.showKeyboard() {
    this.requestFocus()
    val imm: InputMethodManager =
        this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //show keyboard
    imm.showSoftInput(this, 0)
}