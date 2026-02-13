package com.brainx.ktor_network.network_client

import com.brainx.ktor_network.core.headers.EmptyHeaderProvider
import com.brainx.ktor_network.core.headers.HeaderProvider
import com.brainx.ktor_network.core.interfaces.TokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
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

class AppHttpClient(
    private val engine: HttpClientEngine,
    private val tokenStore: TokenProvider,
    private val refreshClient: HttpClient,
    private val headersProvider : HeaderProvider = EmptyHeaderProvider
) {
    fun createKtorHttpClient(): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    useAlternativeNames = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 1000L * 300L
                connectTimeoutMillis = 1000L * 120L
                socketTimeoutMillis = 1000L * 120L
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Network Log: " + message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    println("HTTP status:" + "${response.status.value}")
                }
            }
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 1)
                exponentialDelay()
            }
            install(Auth) {
                bearer {
                    sendWithoutRequest { true }

                    loadTokens {
                        val access = tokenStore.getAccessToken()
                        val refresh = tokenStore.getRefreshToken()
                        if (access != null && refresh != null) BearerTokens(access, refresh) else null
                    }

//                    refreshTokens {
//                        val refresh = tokenStore.getRefreshToken() ?: return@refreshTokens null
//
//                        val response = runCatching {
//                            refreshClient.post(ApiEndpoints.REFRESH_TOKEN) {
//                                contentType(ContentType.Application.Json)
//                                setBody(mapOf("refresh" to refresh))
//                            }.body<AuthResponseModel>()
//                        }.getOrNull() ?: return@refreshTokens null
//
//                        val data = response.data ?: return@refreshTokens null
//                        tokenStore.saveTokens(data.access, data.refresh)
//                        BearerTokens(data.access, data.refresh)
//                    }
                }
            }

            defaultRequest {
                accept(ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                headersProvider.headers().forEach { (key, value) ->
                    headers.append(key, value)
                }
            }
        }
    }
}