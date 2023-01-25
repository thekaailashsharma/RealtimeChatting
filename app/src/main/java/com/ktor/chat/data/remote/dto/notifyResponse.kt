package com.ktor.chat.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class notifyResponse(
    @SerialName("canonical_ids")
    val canonicalIds: Int?,
    @SerialName("failure")
    val failure: Int?,
    @SerialName("multicast_id")
    val multicastId: Long?,
    @SerialName("results")
    val results: List<Result>?,
    @SerialName("success")
    val success: Int?
)