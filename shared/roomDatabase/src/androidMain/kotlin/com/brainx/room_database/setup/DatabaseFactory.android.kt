package com.brainx.room_database.setup

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.brainx.room_database.provider.RoomDatabaseProvider
import com.brainx.room_database.utils.DatabaseConstants

internal actual class DatabaseFactory(
    private val context: Context,
    private val databaseProvider: RoomDatabaseProvider
) {
    actual fun createDatabase(): RoomDatabase.Builder<AppDatabase>  = getDatabaseBuilder(context,databaseProvider)
}

private fun getDatabaseBuilder(context: Context,databaseProvider: RoomDatabaseProvider): RoomDatabase.Builder<AppDatabase> {
    check(databaseProvider.getDatabaseName().isNullOrBlank()){
        "Database name is null. Please provide a valid database name by implementing the interface RoomDatabaseProvider and adding it to DI Module"
    }
    val dbFile = context.applicationContext.getDatabasePath(databaseProvider.getDatabaseName())

    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath,
    )
}