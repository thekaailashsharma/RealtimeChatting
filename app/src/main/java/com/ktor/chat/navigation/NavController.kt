package com.ktor.chat.navigation

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.text.font.lerp
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ktor.chat.presentation.chat.ChatViewModel
import com.ktor.chat.presentation.chat.ui.ChatScreen
import com.ktor.chat.presentation.login.LoginPage
import com.ktor.chat.presentation.login.UIForLogin
import com.ktor.chat.presentation.p2p.ui.GroupChat
import com.ktor.chat.presentation.p2p.ui.ImageUploader
import com.ktor.chat.presentation.p2p.ui.P2PScreen
import com.ktor.chat.presentation.p2p.ui.Particles
import com.ktor.chat.presentation.stories.UserStory
import com.ktor.chat.presentation.users.ui.AllUsers
import com.ktor.chat.presentation.users.ui.ShowUsers
import com.ktor.chat.ui.theme.P2PBackground
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun NavController() {
    val navController = rememberNavController()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(P2PBackground)
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
            AllUsers(from = from, navController = navController)
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
            GroupChat(from = from, to = to, navController = navController)
        }

        composable("GroupChat"){
            var showParticles by remember { mutableStateOf(true) }
            Particles(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                quantity = 22,
                emoji = "\uD83D\uDD25",
                visible = showParticles
            )
        }

        composable("imageUploadScreen/{to}",  arguments = listOf(
            navArgument(name = "to") {
                type = NavType.StringType
                nullable = true
            },
            )){
            val to = it.arguments?.getString("to") ?: ""
            ImageUploader(to = to)
        }

        composable(
            route = "stories/{from}",
            arguments = listOf(
                navArgument(name = "from") {
                    type = NavType.StringType
                    nullable = true
                },

                )
        ) {
            val from = it.arguments?.getString("from") ?: ""
            UserStory(from = from)
        }


    }
}
