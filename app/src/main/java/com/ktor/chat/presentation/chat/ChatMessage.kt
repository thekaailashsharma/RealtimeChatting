package com.ktor.chat.presentation.chat

import com.ktor.chat.data.remote.dto.LocationMessage
import com.ktor.chat.domains.model.Message
import com.ktor.chat.domains.model.P2PMessage

data class ChatMessage(
    val message: List<Message> = emptyList(),
    val isLoading: Boolean = false
)

data class P2PChatMessage(
    val message: List<P2PMessage> = emptyList(),
    val isLoading: Boolean = false
)
data class P2PLocationMessage(
    val message: List<LocationMessage> = emptyList(),
    val isLoading: Boolean = false
)