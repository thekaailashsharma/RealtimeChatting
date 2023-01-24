package com.ktor.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.messaging.FirebaseMessaging
import com.ktor.chat.presentation.chat.ui.ChatScreen
import com.ktor.chat.presentation.p2p.ui.P2PScreen
import com.ktor.chat.presentation.username.ui.UsernameScreen
import com.ktor.chat.presentation.users.ui.ShowUsers
import com.ktor.chat.ui.theme.RealtimeChattingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        setContent {
            RealtimeChattingTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "username_screen"
                ) {
                    composable("username_screen") {
                        UsernameScreen(onNavigate = navController::navigate)
                    }
                    composable(
                        route = "users/{from}",
                        arguments = listOf(navArgument(name = "from"){
                            type = NavType.StringType
                            nullable = false
                        })
                    ){
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
        }
    }
}
