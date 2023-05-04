package com.ktor.chat.presentation.p2p.ui

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ktor.chat.presentation.p2p.data.P2PViewModel
import com.ktor.chat.ui.theme.monteSB

@Composable
fun ImageUploader(to: String, viewModel: P2PViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            println("ImageUploaderUri = ${viewModel.imageUri.value}")
            AsyncImage(
                model = viewModel.imageUri.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
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
                        text = to,
                        color = Color.White,
                        fontFamily = monteSB,
                        modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                    )
                    IconButton(onClick = viewModel::sendMessage) {
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

@Composable
fun DoodleBackground() {
    val colors = listOf(
        Color(0xFFE91E63),
        Color(0xFF2196F3),
        Color(0xFFFFC107),
        Color(0xFF4CAF50),
        Color(0xFF9C27B0),
        Color(0xFFFF5722),
        Color(0xFF673AB7),
        Color(0xFF03A9F4)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val paint = remember {
            Paint().apply {
                isAntiAlias = true
                style = PaintingStyle.Fill
            }
        }

        val path = remember { Path() }
        var lastOffset: Offset? = null

        Canvas(modifier = Modifier.fillMaxSize()){
            val currentOffset = Offset(size.width / 2f, size.height / 2f)

            if (lastOffset != null) {
                paint.color = colors.random()
                path.reset()
                path.moveTo(lastOffset!!.x, lastOffset!!.y)
                path.lineTo(currentOffset.x, currentOffset.y)
                this.drawPath(path,paint.color)
            }

            lastOffset = currentOffset
        }
        // Draw paths as the user drags their finger
        Canvas(modifier = Modifier.fillMaxSize()) {

        }
    }
}


@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    url: Uri? = null,
    link: String? = null,
    onClose: (() -> Unit)? = null,
) {
    Box(Modifier) {
        if (link != null) {
            AsyncImage(
                model = link,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        } else {
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = modifier
                    .aspectRatio(1.3f, true)
                    .clip(RoundedCornerShape(5.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop,
            )
        }
        Icon(imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(5.dp)
                .clip(CircleShape)
                .clickable {
                    onClose?.invoke()
                }
                .size(20.dp)
                .background(Color.Black.copy(0.45f))
                .padding(2.dp),
            tint = Color.White)
    }
}