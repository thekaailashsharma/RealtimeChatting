package com.ktor.chat.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.ktor.chat.presentation.chat.ChatViewModel
import com.ktor.chat.presentation.chat.ui.ChatScreen
import com.ktor.chat.presentation.login.LoginPage
import com.ktor.chat.presentation.login.UIForLogin
import com.ktor.chat.presentation.p2p.ui.P2PScreen
import com.ktor.chat.presentation.users.ui.ShowUsers

@Composable
fun NavController() {
    val navController = rememberNavController()
    val chatViewModel: ChatViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Screens.FirstScreen.route) {
        composable(Screens.LoginScreen.route) {
            LoginPage(viewModel = chatViewModel)
        }
        composable(Screens.FirstScreen.route) {
            UIForLogin(navHostController = navController, viewModel = chatViewModel)
        }
        composable(
            route = "users/{from}",
            arguments = listOf(navArgument(name = "from") {
                type = NavType.StringType
                nullable = false
            })
        ) {
            val from = it.arguments?.getString("from")
            ShowUsers(from = from, navController = navController)
        }
        composable(
            route = "chat_screen/{username}",
            arguments = listOf(
                navArgument(name = "username") {
                    type = NavType.StringType
                    nullable = true
                }
            ), deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://portal.siesgst.ac.in/events"
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            val username = it.arguments?.getString("username")
            ChatScreen(username = username)
        }

        composable(
            route = "p2pScreen/{from}/{to}",
            arguments = listOf(
                navArgument(name = "to") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(name = "from") {
                    type = NavType.StringType
                    nullable = true
                },

                )
        ) {
            val from = it.arguments?.getString("from")
            val to = it.arguments?.getString("to")
            P2PScreen(from = from, to = to)
        }
    }
}