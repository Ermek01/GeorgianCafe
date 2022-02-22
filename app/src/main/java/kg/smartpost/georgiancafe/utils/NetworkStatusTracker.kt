package kg.smartpost.georgiancafe.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class NetworkStatusTracker(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    val networkStatus = callbackFlow<NetworkStatus> {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                offer(NetworkStatus.Unavailable)
            }

            override fun onAvailable(network: Network) {
                offer(NetworkStatus.Available)
            }

            override fun onLost(network: Network) {
                offer(NetworkStatus.Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback)
        }
    }

    init {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() = println("onUnavailable")
            override fun onAvailable(network: Network) = println("onAvailable")
            override fun onLost(network: Network) = println("onLost")
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)
    }


}

inline fun <Result> Flow<NetworkStatus>.map(
    crossinline onUnavailable: suspend () -> Result,
    crossinline onAvailable: suspend () -> Result,
): Flow<Result> = map { status ->
    when (status) {
        NetworkStatus.Unavailable -> onUnavailable()
        NetworkStatus.Available -> onAvailable()
    }
}

sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}

class NetworkStatusViewModel(
    networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {

    val state =
        networkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched },
            )
            .asLiveData(Dispatchers.IO)
}