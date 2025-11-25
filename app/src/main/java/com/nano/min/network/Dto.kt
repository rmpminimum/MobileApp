package com.nano.min.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val role: String = "USER"
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("token_type") val tokenType: String? = "Bearer",
    val expiresIn: Long? = null
)

@Serializable
data class MeResponse(
    val id: String? = null,
    val email: String? = null,
    val role: String? = null
)

@Serializable
data class Chat(
    val id: Long,
    val title: String,
    val users: List<User>,
)

@Serializable
data class Message(
    val id: Long,
    val senderId: Long,
    val timestamp: Long
)

@Serializable
data class User(
    val id: Long,
    val name: String,
    val username: String,
    val email: String
)
