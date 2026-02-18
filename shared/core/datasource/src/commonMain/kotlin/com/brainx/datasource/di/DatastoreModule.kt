package com.brainx.datasource.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.datasource.local_pref.DatastorePreferenceManager
import org.koin.dsl.module

internal val datastorePrefManagerModule = module {
    single {
        DatastorePreferenceManager(
            datastorePreference = get<DataStore<Preferences>>()
        )
    }
}