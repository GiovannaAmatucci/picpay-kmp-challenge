package com.giovanna.amatucci.desafio_android_picpay.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class UserResponse(
    @SerialName("img") val img: String?,
    @SerialName("name") val name: String?,
    @SerialName("id") val id: Int?,
    @SerialName("username") val username: String?
)