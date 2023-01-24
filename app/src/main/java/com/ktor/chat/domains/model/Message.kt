package com.ktor.chat.domains.model

data class Message(
    val text: String,
    val formattedTime: String,
    val userName: String
)

data class P2PMessage(
    val from: String? = null,
    val to: String? = null,
    val text: String? = null,
    val timeStamp: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,

)