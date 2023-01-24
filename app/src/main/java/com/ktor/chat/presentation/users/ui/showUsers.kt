package com.ktor.chat.presentation.users.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ktor.chat.data.remote.dto.UserInfo
import com.ktor.chat.presentation.users.getUsersViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ShowUsers(
    viewModel: getUsersViewModel = hiltViewModel(),
    from: String?,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        viewModel.getUsers(from = from)
        val k = viewModel.getUsers
        var uk by remember {
            mutableStateOf(listOf<UserInfo>())
        }
        LaunchedEffect(Unit) {

            viewModel.getUsers.collectLatest {
                uk = it
            }

        }

        LazyColumn() {
            items(uk) { item ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(
                                "p2pScreen/$from/${item.userName}"
                            )
                        }
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(Color(0xFFACD5FF)),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = item.userName,
                            color = Color.Black,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(15.dp)
                        )
                        Text(
                            text = item.name,
                            color = Color.Black,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(15.dp)
                        )
                        Text(
                            text = item.email,
                            color = Color.Black,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
        }

    }

}