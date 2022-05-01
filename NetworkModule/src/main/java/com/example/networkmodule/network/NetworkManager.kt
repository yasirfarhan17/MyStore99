package com.example.networkmodule.network

import android.annotation.SuppressLint
import android.net.*
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.annotation.RequiresApi

@SuppressLint("MissingPermission")
class NetworkManager(private val connectivityManager: ConnectivityManager) {

    private val listeners by lazy { hashSetOf<NetworkListener>() }


    @RequiresApi(VERSION_CODES.M)
    fun isInternetAvailable(): Boolean {
        return isNetworkConnected()
    }


    @RequiresApi(VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities.also {
            if (it != null) {
                if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                }
            }
        }
        return false
    }


    @RequiresApi(VERSION_CODES.LOLLIPOP)
    inner class NetworkCallbackListener : ConnectivityManager.NetworkCallback() {
        @RequiresApi(VERSION_CODES.M)
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            synchronized(this@NetworkManager) {
                if (isInternetAvailable()) {
                    Log.e("Ok", "Network onAvailable $network")
                    listeners.forEach { it.onAvailability(isInternetAvailable(), network) }
                }
            }
        }

        @RequiresApi(VERSION_CODES.M)
        override fun onLost(network: Network) {
            super.onLost(network)
            synchronized(this@NetworkManager) {
                if (!isInternetAvailable()) {
                    Log.e("Ok","Network onLost $network")
                    listeners.forEach { it.onAvailability(!isInternetAvailable(), network) }
                }
            }
        }
    }

    interface NetworkListener {
        fun onAvailability(
            isAvailable: Boolean,
            network: Network?
        )
    }
}