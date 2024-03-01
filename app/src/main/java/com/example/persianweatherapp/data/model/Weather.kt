package com.example.persianweatherapp.data.model


data class Weather(
    val city: City? = null,
    val cnt: Int? = null,
    val cod: String? = null,
    val list: List<WeatherItem>? = null,
    val message: Double? = null
)