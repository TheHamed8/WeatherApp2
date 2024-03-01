package com.example.persianweatherapp.data.remote

import com.example.persianweatherapp.data.model.Weather
import com.example.persianweatherapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {

    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = API_KEY
    ): Response<Weather>

}