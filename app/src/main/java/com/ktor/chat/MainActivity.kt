package com.ktor.chat

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.messaging.FirebaseMessaging
import com.ktor.chat.navigation.NavController
import com.ktor.chat.presentation.chat.ChatViewModel
import com.ktor.chat.presentation.login.SmsBroadcastReceiver
import com.ktor.chat.presentation.login.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import com.ktor.chat.ui.theme.RealtimeChattingTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.annotation.Nullable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    private lateinit var viewModel: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseDynamicLinks.getInstance().getDynamicLink(Uri.parse("https://ktor.page.link/NLtk")).addOnSuccessListener {
            println("Dynamic Links Received ${it.utmParameters}")
        }
        setContent {
            RealtimeChattingTheme {
                NavController()
                val client = SmsRetriever.getClient(this)
                client.startSmsUserConsent(null)
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
    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener = object : SmsBroadcastReceiverListener {
            override fun onSuccess(intent: Intent?) {
                startActivityForResult(intent, 200)
            }

            override fun onFailure() {}
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }



    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                println("Message $message")
                viewModel.result.value = message
            }
        }
    }

}