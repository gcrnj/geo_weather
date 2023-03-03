package com.gtech.geoweather.common

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.text.SimpleDateFormat
import java.util.*

fun Activity.setupAppBar(title: String, hasBackButton: Boolean) {
    val appBarCustom = AppBarCustom(this, hasBackButton)
    appBarCustom.title = title
}

val Any.REGISTER_INTENTION: String
    get() = "REGISTER_INTENTION"

val Any.IS_EDIT: Int
    get() = 100

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

fun Int.toFormattedTime(): String {
    val date = Date(this.toLong() * 1000)
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return format.format(date)
}

fun Context.loadImageWithGlide(iconCode: String, imageSize: Float, imageView: ImageView) {

    val weatherImageUrl =
        "https://openweathermap.org/img/wn/$iconCode@2x.png"
//                Glide.with(requireContext()).load(weatherImageUrl)
//                    .into(binding.imgWeather)
    Glide.with(this)
        .load(weatherImageUrl)
        .placeholder(com.gtech.geoweather.R.drawable.loading)
        .into(object :
            CustomTarget<Drawable?>(imageSize.toInt(), imageSize.toInt()) {

            fun setDrawable(drawable: Drawable?) {
                imageView.setImageDrawable(drawable)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                setDrawable(placeholder)
            }

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable?>?
            ) {
                setDrawable(resource)
            }
        })
}

val Int.NIGHT: Int
    get() = 0
val Int.AFTERNOON: Int
    get() = 1
val Int.DAY: Int
    get() = 2

fun Int.getTimeOfDay(): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.toLong()

    return when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 6..11 -> DAY
        in 12..17 -> AFTERNOON
        else -> NIGHT
    }
}

fun Context.getContextCompatColor(colorId: Int): Int {
    return ContextCompat.getColor(
        this,
        colorId
    )
}
