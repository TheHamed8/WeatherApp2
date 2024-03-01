package com.example.persianweatherapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.persianweatherapp.data.remote.NetworkResult
import com.example.persianweatherapp.repository.WeatherRepository
import com.example.persianweatherapp.data.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
) : ViewModel() {

    val data = MutableStateFlow<NetworkResult<Weather>>(NetworkResult.Loading())

    fun getWeather(city: String, units: String) {
        viewModelScope.launch {
            data.emit(repository.getWeather(city, units))
        }
    }


}