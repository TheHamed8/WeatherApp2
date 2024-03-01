package com.example.persianweatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.persianweatherapp.data.db.WeatherDao
import com.example.persianweatherapp.data.db.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherDatabaseModule {

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDataBase): WeatherDao =
        weatherDatabase.dao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): WeatherDataBase =
        Room.databaseBuilder(
            context,
            WeatherDataBase::class.java,
            "weather_database"
        )
            .fallbackToDestructiveMigration()
            .build()

}