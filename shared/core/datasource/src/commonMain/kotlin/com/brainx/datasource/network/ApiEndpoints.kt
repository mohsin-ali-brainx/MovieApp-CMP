package com.brainx.datasource.network

object ApiEndpoints {

    private object BaseUrl {
        private const val SERVER_URL = "https://ticket-tribe-dev.brainxdemo.com"
        const val BASE_URL = "$SERVER_URL/api/v1/"

    }

    private object PathSegment {
        // API Path Segments
        const val AUTH = "auth"
        const val USERS = "users"
    }

    object Auth {
        const val REFRESH_TOKEN = "${BaseUrl.BASE_URL}${PathSegment.AUTH}/refresh_token"
        const val LOGIN = "${BaseUrl.BASE_URL}${PathSegment.AUTH}/login"
    }

}