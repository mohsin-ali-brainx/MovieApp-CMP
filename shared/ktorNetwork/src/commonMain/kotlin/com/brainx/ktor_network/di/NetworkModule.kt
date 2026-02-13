package com.brainx.ktor_network.di

import com.brainx.ktor_network.network_client.AppHttpClient
import com.brainx.ktor_network.network_client.RefreshTokenHttpClient
import com.brainx.ktor_network.utils.enums.NetworkModuleEnums
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ktorNetworkModule = module {

    single<HttpClient>(named(NetworkModuleEnums.REFRESH_TOKEN_CLIENT.type)) {
        RefreshTokenHttpClient(
            engine = get()
        ).createRefreshClient()
    }

    single<HttpClient>(named(NetworkModuleEnums.DEFAULT_CLIENT.type)) {
        AppHttpClient(
            engine = get(),
            tokenStore = get(),
            refreshClient = get(qualifier = named(NetworkModuleEnums.REFRESH_TOKEN_CLIENT.type)),
            ).createKtorHttpClient()
    }

}
