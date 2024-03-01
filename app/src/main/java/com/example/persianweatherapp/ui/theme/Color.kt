package com.example.persianweatherapp.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Colors.splash: Color
    @Composable
    get() = if (isLight) Color(0xFFFFFFFF) else Color(0xFFCFD4DA)

val cardBg = Color(0xFF2C2C2C)
val mainBg = Color(0xFF1D1D1D)
val lightBlue = Color(0xFF05A5CB)
