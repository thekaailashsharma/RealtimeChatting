package com.ktor.chat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val isDarkThemeEnabled : Boolean
    @Composable
    get() = isSystemInDarkTheme()
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val loginBackground = Color(0xFFFFFFFF)
val P2PBackground: Color
    @Composable
    get() = if (isDarkThemeEnabled) Color(0xFF1B202D) else Color(0xFFFFFFFF)
val TextColor: Color
    @Composable
    get() = if (isDarkThemeEnabled) Color.White else Color.Black

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val BrightBlue: Color
    @Composable
    get() = if (isDarkThemeEnabled) Color(0xFF4376FF) else Color(0xFF48A7FF)