package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUpload(
    @SerialName("uniqueId")
    val uniqueId: String?
)