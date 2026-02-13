package com.brainx.ktor_network.core.error_handling

enum class Errors(private val code: Int, private val errorText: String) {

    UNKNOWN(1000, "Unknown Error, please try again later"),
    PARSE_ERROR(1001, "Parsing Error, please try again later"),
    NETWORK_ERROR(1002, "Network Error, please try again later"),
    SSL_ERROR(1004, "SSL ERROR, please try again later"),
    INTERNET_UNAVAILABLE(503, "Internet unavailable"),
    UNKNOWN_HOST(1005, "No address associated with hostname"),
    TIMEOUT_ERROR(1006, "Network connection timed out, please try again later"),
    REQUEST_TIMEOUT(408, "Network connection timed out, please try again later"),
    TOO_MANY_REQUESTS(429, "Too many requests at the moment, please try again later"),
    PAGE_NOT_FOUND(400, "Page not found."),
    SERIALIZATION_ERROR(1003, "Page not found."),
    SERVER_ERROR(500, "Server Error"),

    ;

    fun getValue(): String {
        return errorText
    }

    fun getKey(): Int {
        return code
    }

}
