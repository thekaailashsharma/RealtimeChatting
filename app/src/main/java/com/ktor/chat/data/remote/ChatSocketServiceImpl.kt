package com.ktor.chat.data.remote

import com.ktor.chat.data.remote.dto.MessageDto
import com.ktor.chat.domains.model.Message
import com.ktor.chat.util.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    private val client: HttpClient,
) : ChatSocketSession {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(username: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketSession.Endpoints.Chat.url}?userName=$username")
            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Failure(message = "Couldn't establish a connection.")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(message = e.localizedMessage ?: "Unknown error")
        }
    }

    override fun getAllMessage(): Flow<Message> {
        return try {
            socket?.incoming?.receiveAsFlow()?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messagedto = Json.decodeFromString<MessageDto>(json)
                    messagedto.toMessage()
                } ?: emptyFlow()
        } catch (e: Exception) {
            println("Error is ${e.printStackTrace()}")
            emptyFlow()
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun closeSession() {
        socket?.close()
    }
}