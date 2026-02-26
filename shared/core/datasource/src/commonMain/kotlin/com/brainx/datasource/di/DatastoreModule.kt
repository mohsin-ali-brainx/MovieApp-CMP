package com.brainx.datasource.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.brainx.datasource.local_pref.DatastoreFileProviderImp
import com.brainx.datasource.local_pref.DatastorePreferenceManager
import com.brainx.datasource.network.managers.NetworkTokenProviderManager
import com.brainx.ktor_network.core.interfaces.TokenProvider
import com.brainx.local_datastore.provider.DatastoreFileProvider
import org.koin.dsl.bind
import org.koin.dsl.module

internal val datastorePrefManagerModule = module {
    single {
        DatastorePreferenceManager(
            datastorePreference = get<DataStore<Preferences>>()
        )
    }
}