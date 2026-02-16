package com.brainx.ticket_tribe.di

import com.brainx.datasource.di.datasourceModule
import com.brainx.datasource.di.datastorePlatformModule
import com.brainx.datasource.di.datastorePrefModule
import com.brainx.datasource.di.ktorNetworkModule
import com.brainx.datasource.di.ktorPlatformModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coroutineDispatchersModule,

            // Local Datastore Pref
            datastorePlatformModule,
            datastorePrefModule,

            // Datasource
            datasourceModule,

            // Ktor Network
            ktorPlatformModule,
            ktorNetworkModule,


        )
    }
}