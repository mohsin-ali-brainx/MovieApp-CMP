package com.brainx.ktor_network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val ktorPlatformModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
    }