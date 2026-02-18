package com.brainx.ktor_network.utils.enums

enum class NetworkModuleEnums(val type: String) {
    DEFAULT_CLIENT("default_ktor_client"),
    REFRESH_TOKEN_CLIENT("retry_client"),
    API_KEY("Movie-Api-Key"),
    ACCESS_TOKEN("Movie-Access-Token"),

}