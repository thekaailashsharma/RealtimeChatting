package com.ktor.chat.data.remote

import com.google.android.gms.common.api.Response
import com.ktor.chat.data.remote.dto.LocationMessage
import com.ktor.chat.data.remote.dto.notify
import com.ktor.chat.data.remote.dto.notifyResponse
import com.ktor.chat.data.remote.dto.response
import com.ktor.chat.domains.model.Message
import com.ktor.chat.domains.model.P2PMessage
import com.ktor.chat.util.Resource
import kotlinx.coroutines.flow.Flow

interface P2PSession {
    suspend fun initSession(
        from: String?,
        to: String?
    ): Resource<Unit>

    fun getAllMessage(): Flow<P2PMessage>

    suspend fun sendMessage(message: String)

    suspend fun sendLocation(location: String?)

    fun getLocation(): Flow<LocationMessage>

    suspend fun postNotify(request: notify): response

    suspend fun closeSession()

    companion object{
        val Base_URL = "ws://192.168.43.75:8081"
    }

    sealed class Endpoints(val url: String){
        object Chat: Endpoints("$Base_URL/p2p")
    }
}