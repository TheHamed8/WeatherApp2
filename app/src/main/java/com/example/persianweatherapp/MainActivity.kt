package com.example.persianweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.persianweatherapp.navigation.NavGraph
import com.example.persianweatherapp.ui.component.ChangeStatusBarColor
import com.example.persianweatherapp.ui.theme.PersianWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {

            val navController = rememberNavController()
            PersianWeatherAppTheme {
                ChangeStatusBarColor(navController = navController)
                NavGraph(navHostController = navController)
            }
        }
    }
}