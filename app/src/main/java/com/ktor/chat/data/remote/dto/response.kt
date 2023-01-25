package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class response(
    @SerialName("message_id")
    val messageId: Long
)