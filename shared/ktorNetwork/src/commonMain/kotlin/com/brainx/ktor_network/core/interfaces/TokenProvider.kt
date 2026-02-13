package com.brainx.ktor_network.core.interfaces
interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clear()
}

