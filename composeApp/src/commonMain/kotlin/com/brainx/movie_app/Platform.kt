package com.brainx.movie_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform