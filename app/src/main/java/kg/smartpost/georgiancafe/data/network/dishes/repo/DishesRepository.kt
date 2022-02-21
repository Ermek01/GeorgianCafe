package kg.smartpost.georgiancafe.data.network.dishes.repo

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kg.smartpost.georgiancafe.data.network.NetworkDataSource
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.data.network.category.model.ModelCategory
import kg.smartpost.georgiancafe.data.network.dishes.model.ModelDishes
import kg.smartpost.georgiancafe.data.network.home.model.ModelData
import kg.smartpost.georgiancafe.utils.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class DishesRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : BaseRepository() {

    suspend fun getDishes(page: String): Flow<NetworkResponse<ModelDishes>> {
        return flow<NetworkResponse<ModelDishes>> {
            emit(safeApiCall { networkDataSource.getDishes(page) })
        }.flowOn(Dispatchers.IO)
    }

}