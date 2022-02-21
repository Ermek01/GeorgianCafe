package kg.smartpost.georgiancafe.data.local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.smartpost.georgiancafe.utils.PHONE
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val repository: UserPreferences
) : ViewModel() {

    fun savePhone(value: String) {
        viewModelScope.launch {
            repository.putString(PHONE, value)
        }
    }

    fun getPhone(): String? = runBlocking {
        repository.getString(PHONE)
    }

}