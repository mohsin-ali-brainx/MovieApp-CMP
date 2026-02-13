package com.brainx.ktor_network.core.headers

interface HeaderProvider {
    fun headers(): Map<String, String>
}

