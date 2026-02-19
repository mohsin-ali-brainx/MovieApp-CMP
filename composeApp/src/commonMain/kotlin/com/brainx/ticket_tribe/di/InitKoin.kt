package com.brainx.ticket_tribe.di

import com.brainx.bootstrap_di.di.datasourceModuleProvider
import com.brainx.domain.di.domainModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            listOf(coroutineDispatchersModule,viewModelModule) + datasourceModuleProvider + domainModule
        )
    }
}