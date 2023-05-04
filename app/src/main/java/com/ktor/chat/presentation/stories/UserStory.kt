package com.ktor.chat.presentation.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ktor.chat.data.remote.dto.OneStoryItem
import com.ktor.chat.presentation.login.ProfileImage
import com.ktor.chat.presentation.users.getUsersViewModel
import com.ktor.chat.ui.theme.P2PBackground
import com.ktor.chat.ui.theme.TextColor
import com.ktor.chat.ui.theme.monteSB
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStory(from: String, viewModel: getUsersViewModel = hiltViewModel()) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(P2PBackground)) {
        var userStory by remember {
            mutableStateOf<OneStoryItem?>(null)
        }
        LaunchedEffect(key1 = Unit) {
            viewModel.getOneStory(from)
            delay(5000)
            try {
                userStory = viewModel.userStory.value[0]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        println("This is my Story is $userStory")
        if (userStory != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ProfileImage(imageUrl = "http://192.168.31.148:8081/storyImage?username=$from",
                    modifier = Modifier
                        .size(600.dp)
                        .padding(3.dp)
                        .clip(RoundedCornerShape(10.dp)))
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = userStory?.userName ?: "",
                        color = TextColor,
                        fontFamily = monteSB,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 15.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        Text(text = getTimeAgo(userStory?.storyUpdated ?: 0L),
                            color = TextColor,
                            fontFamily = monteSB,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(end = 10.dp))
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBBC0C7))) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                        TextField(value = "",
                            onValueChange = {
                            },
                            placeholder = {
                                Text(text = "Enter a message", color = Color.Black)
                            },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.textFieldColors(textColor = Color.Black,
                                containerColor = Color(0xFFBBC0C7)))
                    }
                }
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp, end = 25.dp), contentAlignment = Alignment.BottomStart) {
                    val compnotify by rememberLottieComposition(
                        spec = LottieCompositionSpec.Url("https://assets2.lottiefiles.com/private_files/lf30_fksszxkz.json"))
                    LottieAnimation(
                        composition = compnotify,
                        iterations = Int.MAX_VALUE,
                        contentScale = ContentScale.Crop,
                        speed = 1.45f,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 10.dp, top = 30.dp)
                    )
                }
            }
        }
    }

}

fun getTimeAgo(timeInMillis: Long): String {
    val currentTime = System.currentTimeMillis()
    val diff = currentTime - timeInMillis
    val minutes = diff / (1000 * 60)
    val hours = minutes / 60

    return when {
        hours > 24 -> "${hours / 24} days ago"
        hours > 0 -> "$hours hours ago"
        else -> "$minutes minutes ago"
    }
}
