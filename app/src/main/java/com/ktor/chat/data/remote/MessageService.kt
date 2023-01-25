package com.ktor.chat.data.remote

import com.ktor.chat.domains.model.Message

interface MessageService {

    suspend fun getAllMessages(): List<Message>

    companion object{
        val Base_URL = "https://192.168.43.75:8081"
    }

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$Base_URL/Messages")
    }
}