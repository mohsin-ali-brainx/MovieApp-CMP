package com.brainx.ktor_network.core.models
import com.brainx.ktor_network.core.error_handling.AppException
import io.ktor.http.Headers

data class DataWithErrorState<out T>(
    val data: T?,
    val error: AppException,
    val headers: Headers
) 