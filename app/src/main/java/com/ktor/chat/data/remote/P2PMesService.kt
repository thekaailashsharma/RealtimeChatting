package com.ktor.chat.data.remote

import com.ktor.chat.domains.model.P2PMessage

interface P2PMesService {
    suspend fun getAllMessages(from: String?, to: String?): List<P2PMessage>

    companion object{
        val Base_URL = "https://192.168.31.148:8081"
    }

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$Base_URL/Messages")
    }
}