package com.ktor.chat.presentation.p2p.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ktor.chat.presentation.login.ProfileImage
import com.ktor.chat.presentation.p2p.data.P2PViewModel
import com.ktor.chat.ui.theme.P2PBackground
import com.ktor.chat.ui.theme.TextColor
import com.ktor.chat.ui.theme.monteSB
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChat(
    viewModel: P2PViewModel = hiltViewModel(),
    from: String?,
    to: String?,
    navController: NavController,
) {
    var myFile by remember {
        mutableStateOf<File?>(null)
    }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var isURIVisible by remember {
        mutableStateOf<Boolean>(false)
    }
    var ok by remember {
        mutableStateOf<String?>(null)
    }
    println("Ok is $ok")
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        println("My Uri is $uri")
        ok = uri?.let { viewModel.onFilePathsListChange(context = context, list = it) }
        selectedImageUri = uri
        viewModel.imageUri.value = uri
    }
    LaunchedEffect(key1 = selectedImageUri) {
        if (selectedImageUri != null) {
            isURIVisible = true
        }
    }
//    LaunchedEffect(key1 = viewModel.uniqueId.value) {
//        println("Came till here $viewModel.uniqueId.value")
//        viewModel.messageText.value =
//            "http://192.168.31.148:8081/upload?uniqueID=src/main/resources/profiles/image.jpg"
//        isURIVisible = false
//        viewModel::sendMessage.invoke()
//    }
    BackHandler() {
        if (isURIVisible) {
            isURIVisible = false
        } else {
            isURIVisible = false
            navController.popBackStack()
        }
    }
    if (isURIVisible) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(P2PBackground)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = viewModel.imageUri.value,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                IconButton(onClick = {
                    isURIVisible = false
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFBBC0C7)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = to ?: "Danny Hapkins",
                            color = TextColor,
                            fontFamily = monteSB,
                            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd) {
                            IconButton(onClick = {
                                val filesDir = context.cacheDir
                                val resolver = context.contentResolver.openFileDescriptor(
                                    Uri.fromFile(File(ok!!)), "r",
                                )
                                val inputStream = FileInputStream(resolver?.fileDescriptor)
                                inputStream.use {
                                    val file = File(filesDir, "image.jpg")
                                    val outputStream = FileOutputStream(file)
                                    inputStream.copyTo(outputStream)
                                    resolver?.close()
                                    myFile = file
                                }
                                myFile?.let { viewModel.uploadImage(it) }
                                viewModel.messageText.value =
                                    "http://192.168.31.148:8081/upload?uniqueID=src/main/resources/profiles/image.jpg"
                                isURIVisible = false
                                viewModel::sendMessage.invoke()
                                viewModel.imageUri.value = null
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(P2PBackground)) {
            LaunchedEffect(key1 = true) {
                viewModel.toastEvent.collectLatest { message ->
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
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 15.dp),
                verticalAlignment = Alignment.CenterVertically) {
                ProfileImage(
                    imageUrl = "http://192.168.31.148:8081/profile?username=$to",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(3.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = ("$to") ?: "Danny Hapkins",
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
                    )
                }
            }
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
                    println("Message.from = ${message.from} & to = ${message.to}")
                    if (to == message.to) {
                        println("Yesss")
                        Box(
                            contentAlignment = if (isOwnMessage) {
                                Alignment.CenterEnd
                            } else Alignment.CenterStart,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (checkLink(message.text.toString())) {
                                val link = getLink(message.text.toString())
                                println("The given link is $link")
                                ProfileImage(
                                    imageUrl = link,
                                    modifier = Modifier
                                        .size(250.dp)
                                        .padding(3.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                            } else {
                                Column(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .padding(horizontal = 7.dp)
                                        .background(
                                            color = if (isOwnMessage) Color(0xFF7A8194)
                                            else Color(0xFF373E4E),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    if (message.text.toString() == "❤️") {
                                        println("Read heart emoji found")
                                        val compnotify by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("heart.json"))
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
                                    else if (message.text.toString() == "😂") {
                                        println("Read heart emoji found")
                                        val compnotify by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("laugh.json"))
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
                                    else if (message.text.toString() == "😉") {
                                        println("Read heart emoji found")
                                        val compnotify by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("wink.json"))
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
                                    else if (message.text.toString() == "😋") {
                                        println("Read heart emoji found")
                                        val compnotify by rememberLottieComposition(
                                            spec = LottieCompositionSpec.Asset("tasty.json"))
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
                                    else {
                                        Text(
                                            text = message.text.toString(),
                                            color = TextColor,
                                        )
                                    }
                                    Text(
                                        text = message.timeStamp.toString(),
                                        color = TextColor,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFBBC0C7)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        launcher.launch("image/*")
//                    navController.navigate("imageUploadScreen/$to/$selectedImageUri")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Camera",
                            tint = Color.Black
                        )
                    }
                    TextField(
                        value = viewModel.messageText.value,
                        onValueChange = viewModel::onMessageChange,
                        placeholder = {
                            Text(text = "Enter a message", color = Color.Black)
                        },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black, containerColor = Color(0xFFBBC0C7)
                        )
                    )
                    IconButton(onClick = viewModel::sendMessage) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.Black
                        )
                    }
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

}

fun checkLink(link: String): Boolean {
    val regex = """\bhttps?://\S+\b""".toRegex()
    val matchResult = regex.find(link)
    if (matchResult != null) {
        return true
    }
    return false
}

fun getLink(link: String): String? {
    val regex = """\bhttps?://\S+\b""".toRegex()
    val matchResult = regex.find(link)
    if (matchResult != null) {
        return matchResult.value
    }
    return null
}

fun hasEmoji(str: String): Boolean {
    val emojiPattern = "[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+".toRegex()
    val matchResult = emojiPattern.findAll(str)
    return matchResult.count() == 1 && str.replace(emojiPattern, "").isEmpty()
}

fun extractEmojis(text: String): String? {
    val emojiPattern = Regex("["
            + "\\u2190-\\u21FF"
            + "\\u2300-\\u23FF"
            + "\\u25A0-\\u27BF"
            + "\\u2B00-\\u2BFF"
            + "\\u3000-\\u303F"
            + "\\u1F000-\\u1F9FF"
            + "\\uD83C[\\uDF00-\\uDFFF]"
            + "\\uD83D[\\uDC00-\\uDE4F]"
            + "\\uD83E[\\uDD00-\\uDDFF]"
            + "]+")

    return if (emojiPattern.findAll(text).count() == 1 && text.replace(emojiPattern, "").isEmpty())
        emojiPattern.findAll(text).joinToString("")
    else
        null

}

fun isRedHeartEmoji(emoji: String): Boolean {
    val redHeart = "\u2764" // Unicode value for red heart emoji
    return emoji == redHeart
}

