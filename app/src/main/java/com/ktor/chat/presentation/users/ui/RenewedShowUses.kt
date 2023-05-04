package com.ktor.chat.presentation.users.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ktor.chat.data.remote.dto.UserInfo
import com.ktor.chat.presentation.login.ProfileImage
import com.ktor.chat.presentation.users.getUsersViewModel
import com.ktor.chat.ui.theme.P2PBackground
import com.ktor.chat.ui.theme.TextColor
import com.ktor.chat.ui.theme.monteSB
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AllUsers(
    viewModel: getUsersViewModel = hiltViewModel(),
    from: String?,
    navController: NavController,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(P2PBackground)) {
        viewModel.getUsers(from = from)
        var uk by remember {
            mutableStateOf(listOf<UserInfo>())
        }
        LaunchedEffect(Unit) {
            viewModel.getAllStories(from ?: "")
            viewModel.getUsers.collectLatest {
                uk = it
            }

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Your Networx",
                color = TextColor,
                fontFamily = monteSB,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "",
                    tint = TextColor,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .size(30.dp)
                        .clickable {
                            viewModel.getOneStory(from ?: "")
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 15.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Recent Stories",
                color = TextColor,
                fontFamily = monteSB,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "",
                    tint = TextColor,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .size(30.dp)
                        .clickable {
                            viewModel.getAllStories(from ?: "")
                        }
                )
            }
        }
        LazyRow(contentPadding = PaddingValues(15.dp)) {
            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileImage(
                        imageUrl = "http://192.168.31.148:8081/profile?username=${from}",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(3.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = "New Story",
                        color = TextColor,
                        fontFamily = monteSB,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                    )
                }
            }
            items(viewModel.listOfStories.value) { story ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        navController.navigate("stories/${story.userName}")
                    }
                ) {
                    ProfileImage(
                        imageUrl = "http://192.168.31.148:8081/profile?username=${story.userName}",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(3.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = story.userName,
                        color = TextColor,
                        fontFamily = monteSB,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme())
                Color(0xFF292F3F) else Color(0xFFCED0D1)),
            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
            elevation = CardDefaults.cardElevation(25.dp)
        ) {
            Text(
                text = "Messages",
                color = TextColor,
                fontFamily = monteSB,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp)
            )
            LazyColumn(contentPadding = PaddingValues(9.dp)) {
                items(uk) { item ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                "p2pScreen/$from/${item.userName}"
                            )
                        }
                        .padding(start = 15.dp, top = 15.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        ProfileImage(
                            imageUrl = "http://192.168.31.148:8081/profile?username=${item.userName}",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(3.dp)
                                .clip(CircleShape)
                        )
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = item.name,
                                color = TextColor,
                                fontFamily = monteSB,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                            )
                            Text(
                                text = item.userName,
                                color = TextColor,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

