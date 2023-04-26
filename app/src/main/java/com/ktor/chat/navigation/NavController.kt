package com.ktor.chat.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ktor.chat.presentation.chat.ChatViewModel
import com.ktor.chat.presentation.login.LoginPage
import com.ktor.chat.presentation.username.ui.OpenScreen

@Composable
fun NavController() {
    val navController = rememberNavController()
    val viewModel: ChatViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Screens.FirstScreen.route){
        composable(Screens.LoginScreen.route){
            LoginPage(viewModel = viewModel)
        }
        composable(Screens.FirstScreen.route){
            OpenScreen(navController = navController)
        }
    }
}