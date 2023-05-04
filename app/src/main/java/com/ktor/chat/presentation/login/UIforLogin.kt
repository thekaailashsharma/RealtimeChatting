package com.ktor.chat.presentation.login

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ktor.chat.data.remote.dto.AllStoriesItem
import com.ktor.chat.navigation.Screens
import com.ktor.chat.presentation.chat.ChatViewModel
import com.ktor.chat.presentation.users.getUsersViewModel
import com.ktor.chat.ui.theme.P2PBackground
import com.ktor.chat.ui.theme.TextColor
import com.ktor.chat.ui.theme.monteSB
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import java.io.File

@Composable
fun UIForLogin(
    navHostController: NavHostController,
    viewModel: ChatViewModel,
    getUsersViewModel: getUsersViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var myFile by remember {
        mutableStateOf<File?>(null)
    }
    var ok by remember {
        mutableStateOf<String?>(null)
    }
    println("LoaderURI = $selectedImageUri")
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
        ok =
            selectedImageUri?.let {
                getUsersViewModel.onFilePathsListChange(context = context,
                    list = it)
            }

    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(P2PBackground)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Image(
                painter = painterResource(id = com.ktor.chat.R.drawable.loginnnnnn),
                contentDescription = null,
                modifier = Modifier.padding(top = 50.dp)
            )
        }
        val iconSize = 65.dp
        val offsetInPx = LocalDensity.current.run { (iconSize / 0.620f).roundToPx() }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                colors = CardDefaults.cardColors(Color(0xFFD9D9D9).copy(0.4f)),
                shape = RoundedCornerShape(15.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            color = Color(0xFF4885ED),
                            fontFamily = monteSB,
                        )) {
                            append("Welcome")
                        }
                        append(" ")
                        withStyle(SpanStyle(
                            color = Color(0xFFF4C20D).copy(0.89f),
                            fontFamily = monteSB
                        )) {
                            append("to")
                        }
                    }, fontSize = 25.sp)
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            color = Color(0xFF1DE9B6),
                            fontFamily = monteSB,
                        )) {
                            append("Networx")
                        }
                        append(" ")
                    }, fontSize = 25.sp)
                    Spacer(modifier = Modifier.height(30.dp))

                    Card(
                        colors = CardDefaults.cardColors(P2PBackground),
                        elevation = CardDefaults.cardElevation(0.dp),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 7.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                "SignUp",
                                color = TextColor,
                                fontFamily = monteSB,
                                fontSize = 19.sp,
                                modifier = Modifier
                                    .padding(start = 17.dp)
                                    .clickable {
                                        navHostController.navigate(Screens.LoginScreen.route)
                                    }
                            )
                            Text(
                                "Login",
                                color = TextColor,
                                fontFamily = monteSB,
                                fontSize = 19.sp,
                                modifier = Modifier
                                    .padding(end = 17.dp)
                                    .clickable {
                                        navHostController.navigate("users/helloDanny")
                                    }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(x = 0, y = -offsetInPx)
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Icon(
                    painter = painterResource(id = com.ktor.chat.R.drawable.vector),
                    contentDescription = null,
                    tint = TextColor,
                    modifier = Modifier.size(iconSize)
                )
            }

        }
    }
}

