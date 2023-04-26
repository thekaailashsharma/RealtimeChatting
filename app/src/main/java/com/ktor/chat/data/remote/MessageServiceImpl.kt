package com.ktor.chat.data.remote

import com.ktor.chat.data.remote.dto.MessageDto
import com.ktor.chat.data.remote.dto.UserInfo
import com.ktor.chat.domains.model.Message
import com.ktor.chat.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow


class MessageServiceImpl(
    private val client: HttpClient
): MessageService {
    override suspend fun getAllMessages(): List<Message> {
        return try {
            client.get<List<MessageDto>>(MessageService.Endpoints.GetAllMessages.url)
                .map {
                    it.toMessage()
                }
        } catch (e: Exception){
            println("Error is ${e.printStackTrace()}")
            return emptyList()
        }

    }
}

class GetUsersImpl(
    private val client: HttpClient
): ListOfUsers{
    override suspend fun getAllUsers(from: String?): List<UserInfo> {
        return try {
            client.get<List<UserInfo>>("${ListOfUsers.Endpoints.GetAllUsers.url}?from=$from")

        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun initSession(from: String, to: String): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override fun getAllMessage(): Flow<Message> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: String) {
        TODO("Not yet implemented")
    }

    override suspend fun closeSession() {
        TODO("Not yet implemented")
    }
}