package com.example.persianweatherapp.di

import com.example.persianweatherapp.data.remote.WeatherApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherApiInterfaceModule {

    @Singleton
    @Provides
    fun provideWeatherApiInterface(retrofit: Retrofit): WeatherApiInterface =
        retrofit.create(WeatherApiInterface::class.java)

}