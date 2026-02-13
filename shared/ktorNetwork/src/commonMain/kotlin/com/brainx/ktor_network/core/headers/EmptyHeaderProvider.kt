package com.brainx.ktor_network.core.headers

object EmptyHeaderProvider : HeaderProvider {
    override fun headers(): Map<String, String> = emptyMap()
}

