package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("profile")
    val profile: String,
    @SerialName("userName")
    val userName: String
)