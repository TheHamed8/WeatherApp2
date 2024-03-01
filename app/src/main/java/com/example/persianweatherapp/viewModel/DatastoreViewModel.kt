package com.example.persianweatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.persianweatherapp.data.datastore.DatastoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DatastoreViewModel @Inject constructor(
    private val repository: DatastoreRepository
) : ViewModel() {

    companion object {
        const val USER_UNIT_KEY = "USER_UNIT_KEY"
        const val USER_CITY_KEY = "USER_CITY_KEY"
    }


    fun saveUserUnit(value: String) {
        viewModelScope.launch {
            repository.putString(USER_UNIT_KEY, value)
        }
    }

    fun getUserUnit(): String? = runBlocking {
        repository.getString(USER_UNIT_KEY)
    }

    fun saveUserCity(value: String) {
        viewModelScope.launch {
            repository.putString(USER_CITY_KEY, value)
        }
    }

    fun getUserCity(): String? = runBlocking {
        repository.getString(USER_CITY_KEY)
    }


}
