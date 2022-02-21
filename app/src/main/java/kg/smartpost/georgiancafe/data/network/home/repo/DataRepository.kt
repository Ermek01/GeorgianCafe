package kg.smartpost.georgiancafe.data.network.home.repo

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kg.smartpost.georgiancafe.data.network.NetworkDataSource
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.data.network.home.model.ModelData
import kg.smartpost.georgiancafe.utils.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class DataRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : BaseRepository() {

    suspend fun getData(page: String): Flow<NetworkResponse<ModelData>> {
        return flow<NetworkResponse<ModelData>> {
            emit(safeApiCall { networkDataSource.getData(page) })
        }.flowOn(Dispatchers.IO)
    }

}