package com.brainx.local_datastore.di


import com.brainx.local_datastore.setup.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.local_datastore.provider.DatastoreFileProvider

internal actual val datastorePlatformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore(get<DatastoreFileProvider>()) }
    }