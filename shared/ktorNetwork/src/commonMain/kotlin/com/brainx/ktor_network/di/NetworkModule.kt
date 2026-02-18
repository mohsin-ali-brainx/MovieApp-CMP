package com.brainx.ktor_network.di

import com.brainx.ktor_network.core.interceptor.MovieAuthInterceptor
import com.brainx.ktor_network.network_client.AppHttpClient
import com.brainx.ktor_network.network_client.KtorHttpClient
import com.brainx.ktor_network.network_client.RefreshTokenHttpClient
import com.brainx.ktor_network.utils.contants.NetworkConstants
import com.brainx.ktor_network.utils.enums.NetworkModuleEnums
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val ktorNetworkModule = module {

    single { MovieAuthInterceptor(apiKey = get(qualifier = named(NetworkModuleEnums.API_KEY.type)),) }


//    single<HttpClient>(named(NetworkModuleEnums.REFRESH_TOKEN_CLIENT.type)) {
//        RefreshTokenHttpClient(
//            engine = get()
//        ).createRefreshClient()
//    }
//
//    single<HttpClient>(named(NetworkModuleEnums.DEFAULT_CLIENT.type)) {
//        AppHttpClient(
//            engine = get(),
//            tokenStore = get(),
//            refreshClient = get(qualifier = named(NetworkModuleEnums.REFRESH_TOKEN_CLIENT.type)),
//            ).createKtorHttpClient()
//    }

    single<HttpClient>() {
        KtorHttpClient(
            engine = get(),
            authInterceptor = get(),
        ).createKtorHttpClient()
    }

}
