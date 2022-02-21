package kg.smartpost.georgiancafe.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.smartpost.georgiancafe.data.network.NetworkResponse
import kg.smartpost.georgiancafe.data.network.home.model.ModelData
import kg.smartpost.georgiancafe.data.network.home.repo.DataRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DataViewModel @Inject constructor
    (
    private val dataRepository: DataRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResponse<ModelData>> = MutableLiveData()
    val response: LiveData<NetworkResponse<ModelData>> = _response

    fun getData(page: String) = viewModelScope.launch {
        dataRepository.getData(page).collect { values ->
            _response.value = values
        }
    }
}