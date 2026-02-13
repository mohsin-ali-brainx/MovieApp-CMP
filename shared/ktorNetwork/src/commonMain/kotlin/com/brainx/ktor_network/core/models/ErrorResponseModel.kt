package com.brainx.ktor_network.core.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseModel(
    @SerialName("error") val error:String
)
