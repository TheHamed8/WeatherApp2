package com.example.persianweatherapp.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.persianweatherapp.ui.screen.MainScreen
import com.example.persianweatherapp.ui.screen.SearchScreen
import com.example.persianweatherapp.ui.screen.SettingScreen
import com.example.persianweatherapp.ui.screen.SplashScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Splash.route,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navHostController)
        }

        composable(route = Screen.Main.route + "/{city}",
            arguments = listOf(
                navArgument("city") {
                    type = NavType.StringType
                    defaultValue = " "
                    nullable = true
                }
            )
        ) {
            it.arguments!!.getString("city")?.let { city ->
                MainScreen(
                    navController = navHostController,
                    city = city
                )
            }

        }


        composable(Screen.Search.route) {
            SearchScreen(navController = navHostController)
        }
        composable(Screen.Setting.route) {
            SettingScreen(navController = navHostController)
        }
    }
}