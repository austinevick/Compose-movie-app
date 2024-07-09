package com.austinevick.movieapp.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        else -> false
    }

}
