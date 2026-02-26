package com.brainx.datasource.di

import com.brainx.datasource.network.respository_imp.MovieRepositoryImp
import com.brainx.domain.network.repository.MovieRepository
import com.brainx.ktor_network.utils.enums.ApiKeysModuleEnums
import com.brainx.ktor_network.utils.enums.NetworkModuleEnums
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<MovieRepository> {
        MovieRepositoryImp(
            httpClient = get(),
            apiKey = get(named(ApiKeysModuleEnums.API_KEY.type))
        ) as MovieRepository
    }
}