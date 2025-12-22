package com.nano.min.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*

object Api {

    object Auth {

        fun register(
            client: ApiClient,
            email: String,
            password: String,
            role: String = "USER",
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val req = RegisterRequest(email, password, role)
                    val response: HttpResponse =
                        client.httpClient.post("${client.baseUrl}/api/auth/register") {
                            setBody(req)
                        }
                    if (response.status == HttpStatusCode.Created) {
                        withContext(Dispatchers.Main) { onSuccess() }
                    } else {
                        throw Exception("Registration failed: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }

        fun login(
            client: ApiClient,
            email: String,
            password: String,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val req = LoginRequest(email, password)
                    val response: HttpResponse = client.httpClient.post("${client.baseUrl}/api/auth/login") {
                        setBody(req)
                    }
                    if (response.status == HttpStatusCode.OK) {
                        val body: LoginResponse = response.body()
                        val token = body.accessToken
                        if (!token.isNullOrBlank()) {
                            client.tokenStorage.setToken(token)
                            withContext(Dispatchers.Main) { onSuccess() }
                        } else {
                            throw Exception("No token received")
                        }
                    } else {
                        throw Exception("Login failed: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }
    }

    object User {

        fun me(
            client: ApiClient,
            onSuccess: (MeResponse) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: HttpResponse = client.httpClient.get("${client.baseUrl}/users/me")
                    if (response.status == HttpStatusCode.OK) {
                        val body: MeResponse = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to get user: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }

        fun getUserById(
            client: ApiClient,
            id: Long,
            onSuccess: (UserResponse) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: HttpResponse = client.httpClient.get("${client.baseUrl}/users/$id")
                    if (response.status == HttpStatusCode.OK) {
                        val body: UserResponse = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to get user: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }

        fun searchUsers(
            client: ApiClient,
            query: String,
            onSuccess: (List<UserResponse>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: HttpResponse = client.httpClient.get("${client.baseUrl}/users/search") {
                        parameter("query", query)
                    }
                    if (response.status == HttpStatusCode.OK) {
                        val body: List<UserResponse> = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to search users: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }
    }

    object Chats {

        fun getUserChats(
            client: ApiClient,
            onSuccess: (List<ChatResponse>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: HttpResponse = client.httpClient.get("${client.baseUrl}/chats")
                    if (response.status == HttpStatusCode.OK) {
                        val body: List<ChatResponse> = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to get chats: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }

        fun createChat(
            client: ApiClient,
            userId: Long,
            onSuccess: (ChatResponse) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val req = CreateChatRequest(userId)
                    val response: HttpResponse = client.httpClient.post("${client.baseUrl}/chats") {
                        setBody(req)
                    }
                    if (response.status == HttpStatusCode.Created) {
                        val body: ChatResponse = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to create chat: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }

        fun getChatById(
            client: ApiClient,
            id: Long,
            onSuccess: (ChatResponse) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: HttpResponse = client.httpClient.get("${client.baseUrl}/chats/$id")
                    if (response.status == HttpStatusCode.OK) {
                        val body: ChatResponse = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to get chat: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }
    }

    object Messages {

        fun sendMessage(
            client: ApiClient,
            chatId: Long,
            content: String,
            type: String = "text",
            onSuccess: (MessageResponse) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val req = SendMessageRequest(chatId, content, type)
                    val response: HttpResponse = client.httpClient.post("${client.baseUrl}/messages") {
                        setBody(req)
                    }
                    if (response.status == HttpStatusCode.Created) {
                        val body: MessageResponse = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to send message: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }

        fun getChatMessages(
            client: ApiClient,
            chatId: Long,
            limit: Int = 50,
            offset: Int = 0,
            onSuccess: (List<MessageResponse>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: HttpResponse = client.httpClient.get("${client.baseUrl}/messages/$chatId") {
                        parameter("limit", limit)
                        parameter("offset", offset)
                    }
                    if (response.status == HttpStatusCode.OK) {
                        val body: List<MessageResponse> = response.body()
                        withContext(Dispatchers.Main) { onSuccess(body) }
                    } else {
                        throw Exception("Failed to get messages: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }

        fun markMessageAsRead(
            client: ApiClient,
            messageId: Long,
            onSuccess: () -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: HttpResponse = client.httpClient.patch("${client.baseUrl}/messages/$messageId/read")
                    if (response.status == HttpStatusCode.OK) {
                        withContext(Dispatchers.Main) { onSuccess() }
                    } else {
                        throw Exception("Failed to mark message as read: ${response.status}")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { onFailure(e) }
                }
            }
        }
    }
}
