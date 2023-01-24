package com.ktor.chat.data.remote

import com.ktor.chat.data.remote.dto.P2PMessageDto
import com.ktor.chat.domains.model.P2PMessage
import io.ktor.client.*
import io.ktor.client.request.*

class P2PMesServiceImpl(
    private val client: HttpClient
): P2PMesService {
    override suspend fun getAllMessages(from: String?, to: String?): List<P2PMessage> {
        return try {
            client.get<List<P2PMessageDto>>("${P2PMesService.Endpoints.GetAllMessages.url}?from=$from&to=$to")
                .map {
                    it.toP2PMessage()
                }
        } catch (e: Exception){
            println("Error is ${e.printStackTrace()}")
            return emptyList()
        }

    }
}