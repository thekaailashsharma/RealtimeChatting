package com.ktor.chat.presentation.p2p.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ktor.chat.domains.model.Message
import com.ktor.chat.presentation.p2p.data.P2PViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun P2PScreen(
    from: String?,
    to: String?,
    viewModel: P2PViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest{ message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.connectToChat(from, to)
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val state = viewModel.chatState.value
    val locationState = viewModel.locationState.value
    println("The value of locationnnn is ${state.message}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(state.message) { message ->
                val isOwnMessage = message.from == from
                val isOtherMessage = message.to == to
                println("Message.from = ${message.from} & to = ${message.to}")
                println("from = $from & to = $to")
                if (to == message.to) {
                    println("Yesss")
                    Box(
                        contentAlignment = if (isOwnMessage) {
                            Alignment.CenterEnd
                        }
                        else Alignment.CenterStart,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .width(200.dp)
                                .drawBehind {
                                    val cornerRadius = 10.dp.toPx()
                                    val triangleHeight = 20.dp.toPx()
                                    val triangleWidth = 25.dp.toPx()
                                    val trianglePath = Path().apply {
                                        if (isOwnMessage) {
                                            moveTo(size.width, size.height - cornerRadius)
                                            lineTo(size.width, size.height + triangleHeight)
                                            lineTo(size.width - triangleWidth,
                                                size.height - cornerRadius)
                                            close()
                                        }
                                        println("Yesss part 2")
                                        moveTo(0f, size.height - cornerRadius)
                                        lineTo(0f, size.height + triangleHeight)
                                        lineTo(triangleWidth, size.height - cornerRadius)
                                        close()
                                    }
                                    drawPath(
                                        path = trianglePath,
                                        color = if (isOwnMessage) Color.Green else Color.DarkGray
                                    )
                                }
                                .background(
                                    color = if (isOwnMessage) Color.Green else Color.DarkGray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp)
                        ) {

                                Text(
                                    text = "from = ${message.from.toString()}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "to = ${message.to.toString()}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = message.text.toString(),
                                    color = Color.White
                                )
                                Text(
                                    text = message.timeStamp.toString(),
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.End)
                                )

                                println("Why is this called ")
                                Text(
                                    text = message.latitude.toString(),
                                    color = Color.White
                                )
                                Text(
                                    text = message.latitude.toString(),
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }

                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            items(state.message){locationMessage ->

            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = viewModel.messageText.value,
                onValueChange = viewModel::onMessageChange,
                placeholder = {
                    Text(text = "Enter a message")
                },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = viewModel::sendMessage) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send"
                )
            }
//            IconButton(onClick = viewModel::sendLocation) {
//                Icon(
//                    imageVector = Icons.Default.Send,
//                    contentDescription = "Send"
//                )
//            }
        }
    }
}