package com.nano.min.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextLong

class AuthService(val client: ApiClient) {

    suspend fun register(email: String, password: String, role: String = "USER"): Boolean =
        withContext(Dispatchers.IO) {
            val req = RegisterRequest(email, password, role)
            val response: HttpResponse =
                client.httpClient.post("${client.baseUrl}/api/auth/register") {
                    setBody(req)
                }
            return@withContext response.status == HttpStatusCode.Created
        }

    suspend fun login(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val req = LoginRequest(email, password)
        val response: HttpResponse = client.httpClient.post("${client.baseUrl}/api/auth/login") {
            setBody(req)
        }
        if (response.status == HttpStatusCode.OK) {
            val body: LoginResponse = response.body()
            val token = body.accessToken
            if (!token.isNullOrBlank()) {
                client.tokenStorage.setToken(token)
                return@withContext true
            }
        }
        return@withContext false
    }

    suspend fun me(): MeResponse? = withContext(Dispatchers.IO) {
        val response: HttpResponse = client.httpClient.get("${client.baseUrl}/api/me")
        if (response.status == HttpStatusCode.OK) {
            return@withContext response.body()
        }
        return@withContext null
    }

    suspend fun netLag() {
        val delayTime = Random.nextLong(60L..300L)
        delay(delayTime)
    }

    suspend fun getChats(userId: Int) {
        netLag()

    }
}
