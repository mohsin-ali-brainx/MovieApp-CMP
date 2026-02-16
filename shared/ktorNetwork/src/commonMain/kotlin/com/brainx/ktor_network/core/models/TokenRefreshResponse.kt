package com.brainx.ktor_network.core.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TokenRefreshResponse(
    @SerialName("data") val data: TokenRefreshDataModel?,
    @SerialName("message") val message: String? = null
)

@Serializable
data class TokenRefreshDataModel(
    @SerialName("access") val newAccessToken: String? = null,
    @SerialName("refresh") val newRefreshToken: String? = null,
)

