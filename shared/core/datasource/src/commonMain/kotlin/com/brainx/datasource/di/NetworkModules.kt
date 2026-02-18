package com.brainx.datasource.di


import com.brainx.datasource.network.managers.NetworkTokenProviderManager
import com.brainx.ktor_network.core.interfaces.TokenProvider
import com.brainx.ktor_network.utils.enums.NetworkModuleEnums
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


internal val networkTokenProviderModule = module {
    single {
        NetworkTokenProviderManager(get())
    }.bind<TokenProvider>()
}


internal val movieConfigModule = module {
    single<String>(named(NetworkModuleEnums.API_KEY.type)) {
        "f45e86744eeafcb6af043c7d57f55308"
    }

    single<String>(named(NetworkModuleEnums.ACCESS_TOKEN.type)) {
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmNDVlODY3NDRlZWFmY2I2YWYwNDNjN2Q1N2Y1NTMwOCIsIm5iZiI6MTc1MDAyODYyOS43Miwic3ViIjoiNjg0ZjUxNTU1MTE2MTM3MjUzM2ZlODMwIiwic2NvcGVzIjpbImFwaV9yZWFkIl0sInZlcnNpb24iOjF9.UaBGkear32uHG9Ko8dT9qCy9fAHyQOc15qXmZD3f4fA"
    }
}