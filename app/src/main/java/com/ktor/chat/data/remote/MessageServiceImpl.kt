package com.ktor.chat.data.remote

import com.ktor.chat.data.remote.dto.*
import com.ktor.chat.domains.model.Message
import com.ktor.chat.util.Resource
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import java.io.File


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
                client.get("${ListOfUsers.Endpoints.GetAllUsers.url}?from=$from")

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

    @OptIn(InternalAPI::class)
    override suspend fun sendImage(file: File) {
        try {
            client.post<UploadImage>("http://192.168.31.148:8081/users"){
                this.body = client.submitFormWithBinaryData(
                    url = "http://192.168.31.148:8081/users",
                    formData = formData {
                        //the line below is just an example of how to send other parameters in the same request
                        append("name", "john")
                        append("email", "123456")
                        append("username", "heyfromKailash")
                        append("profile", file.readBytes(), Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.Json)
                            append(HttpHeaders.ContentDisposition, "filename=image.jpeg")
                        })
                    }
                ) {
                    onUpload { bytesSentTotal, contentLength ->
                        println("Sent $bytesSentTotal bytes from $contentLength")
                    }
                }
                this.method = HttpMethod.Post
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getAllStories(username: String): Flow<AllStoriesItem> {
        return try {
            val pk =  client.get<List<AllStoriesItem>>("http://192.168.31.148:8081/allStories?username=$username")
                .asFlow()
            println("Stories are my ${pk.toList()}")
            return pk
        } catch (e: Exception){
            e.printStackTrace()
            println("Stories are Lossy")
            emptyFlow()
        }
    }

    override suspend fun getUserStory(username: String): List<OneStoryItem> {
        return try {
            val me =  client.get<List<OneStoryItem>>("http://192.168.31.148:8081/story?username=$username")
            println("One Story is $me")
            return me
        } catch (e: Exception){
            e.printStackTrace()
            OneStory()
        }
    }

    override suspend fun closeSession() {
        TODO("Not yet implemented")
    }
}