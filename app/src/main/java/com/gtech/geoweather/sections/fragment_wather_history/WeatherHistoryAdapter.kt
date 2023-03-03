package com.gtech.geoweather.sections.fragment_wather_history

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gtech.geoweather.R
import com.gtech.geoweather.common.loadImageWithGlide
import com.gtech.geoweather.common.toFormattedTime
import com.gtech.geoweather.databinding.LayoutHistoryBinding
import com.gtech.geoweather.databinding.LayoutHistoryItemsBinding
import com.gtech.geoweather.models.WeatherDatabaseData

class WeatherHistoryAdapter(
    private val activity: Activity,
    private val hashWeathers: Map<String, List<WeatherDatabaseData?>>
) :
    RecyclerView.Adapter<WeatherHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = LayoutHistoryBinding.inflate(activity.layoutInflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val holderBinding = holder.binding


        val dateString = hashWeathers.keys.toList()[position]
        holderBinding.txtDate.text = dateString

        val weatherList = hashWeathers[dateString]
        weatherList?.forEach { weather ->
            Log.d("WeatherHAdapter", weather.toString())

            if (weather != null) {
                val itemBinding = LayoutHistoryItemsBinding.inflate(
                    activity.layoutInflater,
                    holderBinding.linearItems,
                    true
                )
                val textCapWordsDescription = weather.description.split(" ")
                    .joinToString(" ") { it.replaceFirstChar { firstChar -> firstChar.uppercase() } }

                itemBinding.txtTimeAndPlace.text = activity.getString(
                    R.string.time_at_city_country,
                    weather.dateTime.toFormattedTime(),
                    weather.cityName,
                    weather.country
                )
                //Temperature in Celsius
                val temp = activity.getString(
                    R.string.degree_celsius,
                    weather.temperature.toString()
                )
                itemBinding.txtCurrentTemperature.text = temp
                itemBinding.txtWeatherDesc.text = textCapWordsDescription
                itemBinding.txtSunrise.text = weather.sunrise.toFormattedTime()
                itemBinding.txtSunset.text = weather.sunset.toFormattedTime()

                val imageSize = activity.resources.getDimension(com.intuit.sdp.R.dimen._50sdp)
                activity.loadImageWithGlide(weather.icon, imageSize, itemBinding.imgWeather)

            }
        }
    }

    override fun getItemCount(): Int {
        return hashWeathers.size
    }

    inner class HistoryViewHolder(val binding: LayoutHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}