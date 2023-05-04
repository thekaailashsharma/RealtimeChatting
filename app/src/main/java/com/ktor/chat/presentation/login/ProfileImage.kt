package com.ktor.chat.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ktor.chat.R
import com.ktor.chat.ui.theme.BrightBlue
import com.ktor.chat.ui.theme.toSp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.glide.GlideRequestType

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imageUrl: Any? = null,
    initial: Char? = null,
    onClick: (() -> Unit)? = null
) {
    if (imageUrl != null) {
        GlideImage(
            imageModel = {imageUrl}, // loading a network image using an URL.
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit,
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
                        println("Image is Success")
                    }
                    is GlideImageState.Failure -> {
                        println("Image is Failure")
                    }
                }
            },
            modifier = modifier
                .then(
                    onClick?.let {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { it.invoke() }
                    } ?: run {
                        Modifier
                    }
                ),
        )
    } else {
        BoxWithConstraints(
            modifier = modifier
                .then(
                    onClick?.let {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { it.invoke() }
                    } ?: run {
                        Modifier
                    }
                )
                .background(BrightBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial?.uppercase() ?: "",
                fontWeight = FontWeight.SemiBold,
                fontSize = (min(maxWidth, maxHeight) *0.4f).toSp(),
                color = Color.White
            )
        }
    }
}