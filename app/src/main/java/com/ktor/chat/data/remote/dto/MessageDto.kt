package com.ktor.chat.data.remote.dto

import com.ktor.chat.domains.model.Message
import com.ktor.chat.domains.model.P2PMessage
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class MessageDto(
    val text: String,
    val timeStamp: Long,
    val userName: String,
    val id: String,
) {
    fun toMessage(): Message {
        val date = Date(timeStamp)
        val formattedDate = DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)
        return Message(
            text = text,
            formattedTime = formattedDate,
            userName = userName
        )
    }
}

@Serializable
data class UserInfo(
    val userName: String,
    val name: String,
    val email: String
)

@Serializable
data class LocationMessage(
    val from: String? = null,
    val to: String? = null,
    val text: String? = null,
    val timeStamp: Long? = null,
    val latitude: String?,
    val longitude: String?,
    val id:String
)

@Serializable
data class P2PMessageDto(
    val from: String? = null,
    val to: String? = null,
    val text: String? = null,
    val timeStamp: Long? = null,
    val latitude: String?= null,
    val longitude: String? = null,
    val id:String
){
    fun toP2PMessage(): P2PMessage {
        val date = timeStamp?.let { Date(it) }
        val formattedDate = date?.let {
            DateFormat
                .getDateInstance(DateFormat.DEFAULT)
                .format(it)
        }
        return P2PMessage(
            text = text,
            from = from,
            to = to,
            timeStamp = formattedDate,
            longitude = longitude,
            latitude = latitude
        )
    }
    fun toLocationMessage(): P2PMessage {
        return P2PMessage(
            longitude = longitude,
            latitude = latitude
        )
    }
}

