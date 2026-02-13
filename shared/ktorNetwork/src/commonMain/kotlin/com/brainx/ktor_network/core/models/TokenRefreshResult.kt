package com.brainx.ktor_network.core.models

data class TokenRefreshResult(
    val accessToken: String,
    val refreshToken: String
)

