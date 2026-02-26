package com.brainx.datasource.di

import com.brainx.datasource.local_database.RoomDatabaseProviderImp
import com.brainx.datasource.local_pref.DatastoreFileProviderImp
import com.brainx.local_datastore.provider.DatastoreFileProvider
import com.brainx.room_database.provider.RoomDatabaseProvider
import org.koin.dsl.bind
import org.koin.dsl.module

internal val appConfigModule = module {
    single {
        DatastoreFileProviderImp()
    }.bind<DatastoreFileProvider>()

    single {
        RoomDatabaseProviderImp()
    }.bind<RoomDatabaseProvider>()
}