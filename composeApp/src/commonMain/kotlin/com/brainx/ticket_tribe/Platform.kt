package com.brainx.ticket_tribe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform