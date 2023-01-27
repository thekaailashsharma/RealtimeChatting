package com.ktor.chat.navigation

sealed class Screens(val route: String){
    object FirstScreen: Screens("firstScreen")
    object LoginScreen: Screens("loginScreen")
    object SignUpScreen: Screens("signupScreen")
}
