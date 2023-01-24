package com.ktor.chat.data.remote

import com.ktor.chat.data.remote.dto.UserInfo
import com.ktor.chat.domains.model.Message
import com.ktor.chat.util.Resource
import kotlinx.coroutines.flow.Flow

interface ListOfUsers{
    suspend fun getAllUsers(from: String?): List<UserInfo>

    suspend fun initSession(
        from: String,
        to:String
    ): Resource<Unit>

    fun getAllMessage(): Flow<Message>

    suspend fun sendMessage(message: String)

    suspend fun closeSession()

    companion object{
        val Base_URL = "http://192.168.3.75:8081"
    }

    sealed class Endpoints(val url: String){
        object GetAllUsers: Endpoints("$Base_URL/users")
    }
}
