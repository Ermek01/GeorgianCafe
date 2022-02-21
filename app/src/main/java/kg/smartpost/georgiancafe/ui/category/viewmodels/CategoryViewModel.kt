package kg.smartpost.georgiancafe.ui.category.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.data.network.category.model.ModelCategory
import kg.smartpost.georgiancafe.data.network.category.repo.CategoryRepository
import kg.smartpost.georgiancafe.data.network.home.model.ModelData
import kg.smartpost.georgiancafe.data.network.home.repo.DataRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor
    (
    private val categoryRepository: CategoryRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _categories: MutableLiveData<NetworkResponse<ModelCategory>> = MutableLiveData()
    val categories: LiveData<NetworkResponse<ModelCategory>> = _categories

    fun getCategory(page: String) = viewModelScope.launch {
        categoryRepository.getCategory(page).collect { values ->
            _categories.value = values
        }
    }
}