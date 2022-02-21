package kg.smartpost.georgiancafe.data.network.category.repo

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kg.smartpost.georgiancafe.data.network.NetworkDataSource
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.data.network.category.model.ModelCategory
import kg.smartpost.georgiancafe.data.network.home.model.ModelData
import kg.smartpost.georgiancafe.utils.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class CategoryRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : BaseRepository() {

    suspend fun getCategory(page: String): Flow<NetworkResponse<ModelCategory>> {
        return flow<NetworkResponse<ModelCategory>> {
            emit(safeApiCall { networkDataSource.getCategory(page) })
        }.flowOn(Dispatchers.IO)
    }

}