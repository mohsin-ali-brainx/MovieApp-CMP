package com.brainx.datasource.network

internal object ApiEndpoints {

    private object BaseUrl {
        const val SERVER_URL = "https://api.themoviedb.org"
        const val BASE_URL = "$SERVER_URL/3"

    }

    private object PathSegment {
        // API Path Segments
        const val AUTH = "auth"
        const val USERS = "users"
    }

    internal object Auth {
        const val REFRESH_TOKEN = "${BaseUrl.BASE_URL}${PathSegment.AUTH}/refresh_token"
        const val LOGIN = "${BaseUrl.BASE_URL}${PathSegment.AUTH}/login"


    }

    internal object Videos {
        const val SearchMulti = "${BaseUrl.BASE_URL}/search/multi"
        const val MovieVideo = "${BaseUrl.BASE_URL}/movie"
        const val TvVideo = "${BaseUrl.BASE_URL}/tv"
        const val Videos = "videos"
    }

}