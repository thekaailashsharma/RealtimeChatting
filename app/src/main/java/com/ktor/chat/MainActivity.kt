package com.ktor.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.messaging.FirebaseMessaging
import com.ktor.chat.navigation.NavController
import com.ktor.chat.presentation.chat.ui.ChatScreen
import com.ktor.chat.presentation.p2p.ui.P2PScreen
import com.ktor.chat.presentation.username.ui.OpenScreen
import com.ktor.chat.presentation.users.ui.ShowUsers
import com.ktor.chat.ui.theme.RealtimeChattingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse("https://ktor.page.link/NLtk")).addOnSuccessListener {
            println("Dynamic Links Received ${it.utmParameters}")
        }

        setContent {
            RealtimeChattingTheme {
                NavController()
                // A surface container using the 'background' color from the theme
//                val navController = rememberNavController()
//                NavHost(
//                    navController = navController,
//                    startDestination = "username_screen"
//                ) {
//                    composable("username_screen") {
//                        OpenScreen()
//                    }
//                    composable(
//                        route = "users/{from}",
//                        arguments = listOf(navArgument(name = "from"){
//                            type = NavType.StringType
//                            nullable = false
//                        })
//                    ){
//                        val from = it.arguments?.getString("from")
//                        ShowUsers(from = from, navController = navController)
//                    }
//                    composable(
//                        route = "chat_screen/{username}",
//                        arguments = listOf(
//                            navArgument(name = "username") {
//                                type = NavType.StringType
//                                nullable = true
//                            }
//                        ), deepLinks = listOf(
//                            navDeepLink {
//                                uriPattern = "https://portal.siesgst.ac.in/events"
//                                action = Intent.ACTION_VIEW
//                            }
//                        )
//                    ) {
//                        val username = it.arguments?.getString("username")
//                        ChatScreen(username = username)
//                    }
//
//                    composable(
//                        route = "p2pScreen/{from}/{to}",
//                        arguments = listOf(
//                            navArgument(name = "to") {
//                                type = NavType.StringType
//                                nullable = true
//                            },
//                            navArgument(name = "from") {
//                                type = NavType.StringType
//                                nullable = true
//                            },
//
//                        )
//                    ) {
//                        val from = it.arguments?.getString("from")
//                        val to = it.arguments?.getString("to")
//                        P2PScreen(from = from, to = to)
//                    }
//                }

            }
        }
    }
}
