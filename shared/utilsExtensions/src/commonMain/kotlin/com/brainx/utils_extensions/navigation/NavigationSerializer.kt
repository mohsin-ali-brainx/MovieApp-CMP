package com.brainx.utils_extensions.navigation

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonCodec {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        explicitNulls = false
    }
}

inline fun <reified T> T.toJson(): String {
    return JsonCodec.json.encodeToString(this)
}

inline fun <reified T> String.toModel(): T {
    return JsonCodec.json.decodeFromString(this)
}

inline fun <reified T> String.toModelOrNull(): T? {
    return runCatching { JsonCodec.json.decodeFromString<T>(this) }.getOrNull()
}
