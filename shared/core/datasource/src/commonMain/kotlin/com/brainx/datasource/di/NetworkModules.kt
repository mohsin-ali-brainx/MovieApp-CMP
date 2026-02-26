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

