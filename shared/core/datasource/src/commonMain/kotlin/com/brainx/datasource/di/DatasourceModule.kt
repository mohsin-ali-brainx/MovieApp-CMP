package com.brainx.datasource.di



import com.brainx.datasource.network.managers.NetworkTokenProviderManager
import com.brainx.ktor_network.core.interfaces.TokenProvider
import org.koin.dsl.bind
import org.koin.dsl.module


val datasourceModule = module {
    single {
        NetworkTokenProviderManager(get())
    }.bind<TokenProvider>()
}