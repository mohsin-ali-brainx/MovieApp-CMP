package com.brainx.room_database.di

import com.brainx.room_database.setup.AppDatabase
import com.brainx.room_database.setup.DatabaseFactory
import com.brainx.room_database.setup.createAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val roomPlatformModule: Module

val roomDatabaseModule = module {
    single { createAppDatabase(get<DatabaseFactory>().createDatabase()) }
    single { get<AppDatabase>().movieDao() }
//    single<MovieRepository> { MovieRepositoryImpl(get()) }
}