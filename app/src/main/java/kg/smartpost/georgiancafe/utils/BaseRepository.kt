package kg.smartpost.georgiancafe.utils

import kg.smartpost.georgiancafe.data.network.NetworkResponse
import retrofit2.Response

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResponse<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResponse.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResponse<T> =
        NetworkResponse.Error("Api call failed $errorMessage")
}