package com.brainx.room_database.di

import com.brainx.room_database.setup.DatabaseFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val roomPlatformModule: Module = module {
    single { DatabaseFactory(androidApplication(),get()) }
}