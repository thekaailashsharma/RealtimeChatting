package com.ktor.chat.data.remote

import com.ktor.chat.data.remote.dto.LocationMessage
import com.ktor.chat.data.remote.dto.P2PMessageDto
import com.ktor.chat.data.remote.dto.notify
import com.ktor.chat.data.remote.dto.response
import com.ktor.chat.domains.model.P2PMessage
import com.ktor.chat.util.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class P2PServiceImpl(
    private val client: HttpClient,
) : P2PSession {
    private var socket: WebSocketSession? = null
    override suspend fun initSession(from: String?, to: String?): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${P2PSession.Endpoints.Chat.url}?from=$from&to=$to")
            }

            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Failure(message = "Couldn't establish a connection.")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(message = e.localizedMessage ?: "Unknown error")
        }
    }

    override fun getAllMessage(): Flow<P2PMessage> {
        return try {
            socket?.incoming?.receiveAsFlow()?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messagedto = Json.decodeFromString<P2PMessageDto>(json)
                    messagedto.toP2PMessage()
                } ?: emptyFlow()
        } catch (e: Exception) {
            println("Error is ${e.printStackTrace()}")
            emptyFlow()
        }
    }

    override fun getLocation(): Flow<LocationMessage> {
        return try {

            println("Flow of location is ${socket?.incoming?.receiveAsFlow()}")
            socket?.incoming?.receiveAsFlow()?.map {
                val json = (it as? Frame.Text)?.readText() ?: ""
                Json.decodeFromString(json)
            } ?: emptyFlow()
        } catch (e: Exception) {
            println("Error is ${e.printStackTrace()}")
            emptyFlow()
        }
    }

    override suspend fun postNotify(request: notify): response {
        return client.post<response> {
            this.url("https://fcm.googleapis.com/fcm/send")
            this.headers {
                this["Authorization"] =
                    "key=AAAA4iT3W2M:APA91bEZrpoB3N-VnAzuM8au_9tp-mod_UqkBhCgz" +
                            "apCeqZZ873GX08x9egu0VvwFQL468TXP9NuV1vQfQggsADkNfilFiVlH2aY3tP0PMfkbcGmXKmk" +
                            "yMnn-UKRD3qUp0Xd2-tW9d5H"
            }
            this.contentType(ContentType.Application.Json)
            this.body = request
        }

    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun sendLocation(location: String?) {
        try {
            socket?.send(Frame.Text(location.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}