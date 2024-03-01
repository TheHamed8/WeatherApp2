package com.example.persianweatherapp.repository

import com.example.persianweatherapp.data.BaseApiResponse
import com.example.persianweatherapp.data.db.WeatherDao
import com.example.persianweatherapp.data.model.Favorite
import com.example.persianweatherapp.data.model.Unit
import com.example.persianweatherapp.data.remote.NetworkResult
import com.example.persianweatherapp.data.remote.WeatherApiInterface
import com.example.persianweatherapp.data.model.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApiInterface,
    private val dao: WeatherDao
) : BaseApiResponse() {

    suspend fun getWeather(city: String, units: String): NetworkResult<Weather> {
        return safeApiCall {
            api.getWeather(city, units = units)
        }
    }

    fun getFavorites(): Flow<List<Favorite>> = dao.getFavorites()
    suspend fun insertFavorite(favorite: Favorite) = dao.insertFavorite(favorite)
    suspend fun updateFavorite(favorite: Favorite) = dao.updateFavorite(favorite)
    suspend fun deleteAllFavorites() = dao.deleteAllFavorites()
    suspend fun deleteFavorite(favorite: Favorite) = dao.deleteFavorite(favorite)
    suspend fun getFavById(city: String): Favorite = dao.getFavById(city)



}