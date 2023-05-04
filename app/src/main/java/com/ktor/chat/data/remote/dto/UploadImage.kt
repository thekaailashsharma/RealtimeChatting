package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadImage(
    @SerialName("uniqueId")
    val `data`: Data,
    @SerialName("result")
    val result: String
)