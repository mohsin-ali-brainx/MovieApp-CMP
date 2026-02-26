package com.brainx.movie_app.di

import com.brainx.movie_app.app_config.ApiSecrets
import com.brainx.movie_app.utils.enums.NetworkApiKeysModuleEnums
import org.koin.core.qualifier.named
import org.koin.dsl.module

val apiSecretModule = module{
    single<String>(named(NetworkApiKeysModuleEnums.API_KEY.type)) {
        ApiSecrets.movieApiKey
    }
    single<String>(named(NetworkApiKeysModuleEnums.ACCESS_TOKEN.type)) {
        ApiSecrets.movieAccessToken
    }
}