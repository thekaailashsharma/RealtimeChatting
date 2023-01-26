package com.ktor.chat.presentation.username.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ktor.chat.presentation.username.UserNameViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameScreen(
    viewModel: UserNameViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    LaunchedEffect(key1 = true) {
        viewModel.joinChat.collectLatest { username ->
            onNavigate("users/$username")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            imageModel = { "http://192.168.43.75:8081/profile?username=one" }, // loading a network image using an URL.
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            onImageStateChanged = {
                when (it) {
                    GlideImageState.None -> {
                        println("Image is None")
                    }
                    GlideImageState.Loading -> {
                        println("Image is Loading")
                    }
                    is GlideImageState.Success -> {
                        println("Image is Successful")
                    }
                    is GlideImageState.Failure -> {
                        println("Image is Failure")
                    }
                }
            }
        )
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.End
//        ) {
//            TextField(
//                value = viewModel.userName.value,
//                onValueChange = viewModel::onUserNameChange,
//                label = { Text("Enter A UserName ") }
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Button(onClick = viewModel::onJoinClick) {
//                Text(text = "Join")
//            }
//        }
    }
}
