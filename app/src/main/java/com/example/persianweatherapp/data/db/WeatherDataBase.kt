package com.example.persianweatherapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.persianweatherapp.data.model.Favorite
import com.example.persianweatherapp.data.model.Unit

@Database(entities = [Favorite::class, Unit::class], version = 1, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun dao(): WeatherDao
}