package com.example.persianweatherapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.persianweatherapp.navigation.Screen
import com.example.persianweatherapp.ui.theme.mainBg
import com.example.persianweatherapp.util.Constants.USER_CITY
import com.example.persianweatherapp.viewModel.DatastoreViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navController: NavController,
    datastoreViewModel: DatastoreViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBg),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = Icons.Default.Cloud,
            contentDescription = "", tint = Color(0xFF0279B3),
            modifier = Modifier.size(300.dp).padding(bottom = 32.dp)
        )
        Text(text = "Weather", color = Color.White, style = MaterialTheme.typography.displayLarge)

    }
    val defaultCity =
        if (datastoreViewModel.getUserCity()?.isNotEmpty() == true) {
            datastoreViewModel.getUserCity()
        } else {
            "Tehran"
        }
    LaunchedEffect(true) {
        delay(3000)
        navController.navigate(Screen.Main.route + "/$defaultCity") {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }
}