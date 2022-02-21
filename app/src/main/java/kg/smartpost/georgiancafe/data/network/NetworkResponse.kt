package kg.smartpost.georgiancafe.data.network

sealed class NetworkResponse<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResponse<T>(data)

    class Error<T>(message: String, data: T? = null) : NetworkResponse<T>(data, message)

    class Loading<T> : NetworkResponse<T>()

}
