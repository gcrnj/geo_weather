package com.gtech.geoweather.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeatherMapService(private val context: Context) {

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org"
        private const val CACHE_SIZE = (10 * 1024 * 1024).toLong() // 10 MB
    }

    private val cache = Cache(
        // obtain application context here
        context.cacheDir,
        CACHE_SIZE
    )

    private val client = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val request = if (hasNetworkAvailable()) {
                originalRequest.newBuilder().header("Cache-Control", "public, max-age=60").build()
            } else {
                originalRequest.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + (60 * 60 * 24 * 7) // 1 week
                ).build()
            }
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: OpenWeatherMapApi = retrofit.create(OpenWeatherMapApi::class.java)

    private fun hasNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            val hasTransport = activeNetwork::hasTransport
            return when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return false
            return activeNetworkInfo.isConnected
        }
    }

}
