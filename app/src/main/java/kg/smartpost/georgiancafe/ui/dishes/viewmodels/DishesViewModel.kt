package kg.smartpost.georgiancafe.ui.dishes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.data.network.category.model.ModelCategory
import kg.smartpost.georgiancafe.data.network.category.repo.CategoryRepository
import kg.smartpost.georgiancafe.data.network.dishes.model.ModelDishes
import kg.smartpost.georgiancafe.data.network.dishes.repo.DishesRepository
import kg.smartpost.georgiancafe.data.network.home.model.ModelData
import kg.smartpost.georgiancafe.data.network.home.repo.DataRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DishesViewModel @Inject constructor
    (
    private val dishesRepository: DishesRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _dishes: MutableLiveData<NetworkResponse<ModelDishes>> = MutableLiveData()
    val dishes: LiveData<NetworkResponse<ModelDishes>> = _dishes

    fun getDishes(page: String) = viewModelScope.launch {
        dishesRepository.getDishes(page).collect { values ->
            _dishes.value = values
        }
    }
}