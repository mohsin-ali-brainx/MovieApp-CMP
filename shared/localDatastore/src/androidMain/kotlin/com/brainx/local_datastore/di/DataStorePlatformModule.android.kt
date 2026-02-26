package com.brainx.local_datastore.di

import org.koin.core.module.Module

import com.brainx.local_datastore.setup.createDataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences


internal actual val datastorePlatformModule: Module
    get() = module {
        single<DataStore<Preferences>> { createDataStore(androidApplication(),get()) }
    }
