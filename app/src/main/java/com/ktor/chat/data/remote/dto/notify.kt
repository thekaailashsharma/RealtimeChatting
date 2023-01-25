package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class notify(
    @SerialName("notification")
    val notification: NotificationX,
    @SerialName("to")
    val to: String
)