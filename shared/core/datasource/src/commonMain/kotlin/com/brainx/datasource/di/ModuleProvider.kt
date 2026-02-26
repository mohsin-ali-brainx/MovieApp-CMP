package com.brainx.datasource.di

import com.brainx.ktor_network.di.ktorModuleProvider
import com.brainx.local_datastore.di.datastoreModuleProvider
import com.brainx.room_database.di.provideRoomDatabaseModule

val datastoreModulesProvider = listOf(
    networkTokenProviderModule,

    datastorePrefManagerModule,

//    movieConfigModule,

    repositoryModule,

    appConfigModule

) + provideRoomDatabaseModule + ktorModuleProvider + datastoreModuleProvider