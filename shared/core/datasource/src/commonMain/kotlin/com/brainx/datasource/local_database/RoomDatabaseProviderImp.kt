package com.brainx.datasource.local_database

import com.brainx.datasource.AppConfig
import com.brainx.room_database.provider.RoomDatabaseProvider

class RoomDatabaseProviderImp : RoomDatabaseProvider {
    override fun getDatabaseName(): String {
        return AppConfig.RoomDatabase.DATABASE_NAME
    }
}