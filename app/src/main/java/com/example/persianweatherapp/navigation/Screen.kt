package com.example.persianweatherapp.navigation

sealed class Screen(val route:String) {
    data object Main:Screen("main_screen")
    data object Search:Screen("search_screen")
    data object Setting:Screen("setting_screen")
    data object Splash:Screen("splash_screen")
}