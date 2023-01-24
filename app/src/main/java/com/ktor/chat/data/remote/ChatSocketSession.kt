package com.ktor.chat.data.remote

import com.ktor.chat.domains.model.Message
import com.ktor.chat.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketSession {

    suspend fun initSession(
        userName: String
    ): Resource<Unit>

    fun getAllMessage(): Flow<Message>

    suspend fun sendMessage(message: String)

    suspend fun closeSession()

    companion object{
        val Base_URL = "ws://192.168.3.75:8081"
    }

    sealed class Endpoints(val url: String){
        object Chat: Endpoints("$Base_URL/chat")
    }

}