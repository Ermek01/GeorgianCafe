package kg.smartpost.georgiancafe.data.network

import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getData(page: String) =
        apiService.getData(page)

    suspend fun getCategory(page: String) =
        apiService.getCategory(page)

    suspend fun getDishes(page: String) =
        apiService.getDishes(page)

}