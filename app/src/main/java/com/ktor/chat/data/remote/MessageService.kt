package com.ktor.chat.data.remote

import com.ktor.chat.domains.model.Message

interface MessageService {

    suspend fun getAllMessages(): List<Message>

    companion object{
        val Base_URL = "http://192.168.31.148:8081"
    }

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$Base_URL/Messages")
    }
}