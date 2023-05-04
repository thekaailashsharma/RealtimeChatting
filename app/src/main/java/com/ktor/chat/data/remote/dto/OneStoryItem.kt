package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OneStoryItem(
    @SerialName("caption")
    val caption: String,
    @SerialName("image")
    val image: String,
    @SerialName("storyOver")
    val storyOver: Long,
    @SerialName("storyUpdated")
    val storyUpdated: Long,
    @SerialName("userName")
    val userName: String
)