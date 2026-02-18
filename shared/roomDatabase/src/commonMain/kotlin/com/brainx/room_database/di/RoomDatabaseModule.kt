package com.brainx.room_database.di

import com.brainx.room_database.setup.AppDatabase
import com.brainx.room_database.setup.DatabaseFactory
import com.brainx.room_database.setup.createAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val roomPlatformModule: Module

private val roomDatabaseModule = module {
    single { createAppDatabase(get<DatabaseFactory>().createDatabase()) }

}

private val roomDaoModule = module {
    single { get<AppDatabase>().movieDao() }
}


val provideRoomDatabaseModule = listOf( roomPlatformModule,roomDatabaseModule,roomDaoModule )