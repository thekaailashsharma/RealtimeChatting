package com.ktor.chat.data.remote

import com.ktor.chat.data.remote.dto.*
import com.ktor.chat.domains.model.Message
import com.ktor.chat.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ListOfUsers{
    suspend fun getAllUsers(from: String?): List<UserInfo>

    suspend fun initSession(
        from: String,
        to:String
    ): Resource<Unit>

    fun getAllMessage(): Flow<Message>

    suspend fun sendMessage(message: String)

    suspend fun sendImage(file: File)

    suspend fun getAllStories(username: String): Flow<AllStoriesItem>

    suspend fun getUserStory(username: String): List<OneStoryItem>

    suspend fun closeSession()

    companion object{
        val Base_URL = "http://192.168.31.148:8081"
    }

    sealed class Endpoints(val url: String){
        object GetAllUsers: Endpoints("$Base_URL/users")
    }
}
