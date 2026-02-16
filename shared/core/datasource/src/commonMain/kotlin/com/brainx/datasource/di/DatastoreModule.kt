package com.brainx.datasource.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.datasource.local_pref.DatastorePreferenceManager
import com.brainx.local_datastore.di.datastorePlatformModule as localDatastorePlatformModule
import org.koin.core.module.Module
import org.koin.dsl.module

val datastorePlatformModule: Module = localDatastorePlatformModule

val datastorePrefModule = module {
    single {
        DatastorePreferenceManager(
            datastorePreference = get<DataStore<Preferences>>()
        )
    }
}