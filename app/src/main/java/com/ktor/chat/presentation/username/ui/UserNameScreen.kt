package com.ktor.chat.presentation.username.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ktor.chat.R
import com.ktor.chat.navigation.Screens
import com.ktor.chat.presentation.username.UserNameViewModel
import com.ktor.chat.ui.theme.P2PBackground
import com.ktor.chat.ui.theme.TextColor
import com.ktor.chat.ui.theme.loginBackground

@Composable
fun OpenScreen(
    navController: NavController
) {

    Box(modifier = Modifier
        .background(P2PBackground)) {
        Image(
            painter = painterResource(id = R.drawable.dna3),
            contentDescription = "",
            modifier = Modifier.size(1000.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 15.dp, start = 20.dp)
        ) {
            Button(
                onClick = { navController.navigate(Screens.LoginScreen.route) },
                colors = ButtonDefaults.buttonColors(P2PBackground),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.buttonElevation(5.dp)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 15.sp,
                    color = TextColor
                )

            }
        }
    }


//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        GlideImage(
//            imageModel = { "http://192.168.31.148:8081/profile?username=one" }, // loading a network image using an URL.
//            imageOptions = ImageOptions(
//                contentScale = ContentScale.Crop,
//                alignment = Alignment.Center
//            ),
//            onImageStateChanged = {
//                when (it) {
//                    GlideImageState.None -> {
//                        println("Image is None")
//                    }
//                    GlideImageState.Loading -> {
//                        println("Image is Loading")
//                    }
//                    is GlideImageState.Success -> {
//                        println("Image is Successful")
//                    }
//                    is GlideImageState.Failure -> {
//                        println("Image is Failure")
//                    }
//                }
//            }
//        )
////        Column(
////            modifier = Modifier.fillMaxWidth(),
////            verticalArrangement = Arrangement.Center,
////            horizontalAlignment = Alignment.End
////        ) {
////            TextField(
////                value = viewModel.userName.value,
////                onValueChange = viewModel::onUserNameChange,
////                label = { Text("Enter A UserName ") }
////            )
////            Spacer(modifier = Modifier.height(8.dp))
////            Button(onClick = viewModel::onJoinClick) {
////                Text(text = "Join")
////            }
////        }
//    }
}
