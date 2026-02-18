package com.brainx.ktor_network.network_client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


internal class RefreshTokenHttpClient(
    private val engine: HttpClientEngine
) {
    fun createRefreshClient(): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        useAlternativeNames = true
                        ignoreUnknownKeys = true
                        encodeDefaults = false
                    }
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 1000L * 300L
                connectTimeoutMillis = 1000L * 120L
                socketTimeoutMillis = 1000L * 120L
            }
            install(ResponseObserver) {
                onResponse { response ->
                    println("HTTP status:" + "${response.status.value}")
                }
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Network Log: " + message)
                    }
                }
                level = LogLevel.ALL
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 1)
                exponentialDelay()
            }
            defaultRequest {
                // Content Type
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
            }
        }
    }
}