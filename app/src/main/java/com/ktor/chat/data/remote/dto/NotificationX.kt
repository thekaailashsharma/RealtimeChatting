package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationX(
    @SerialName("body")
    val body: String,
    @SerialName("mutable_content")
    val mutableContent: Boolean,
    @SerialName("sound")
    val sound: String,
    @SerialName("title")
    val title: String
)