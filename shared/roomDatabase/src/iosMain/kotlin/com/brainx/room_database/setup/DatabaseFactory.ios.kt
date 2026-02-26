package com.brainx.room_database.setup

import androidx.room.Room
import androidx.room.RoomDatabase
import com.brainx.room_database.provider.RoomDatabaseProvider
import com.brainx.room_database.utils.DatabaseConstants
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual class DatabaseFactory(
    private val databaseProvider: RoomDatabaseProvider
) {
    actual fun createDatabase(): RoomDatabase.Builder<AppDatabase>  = getDatabaseBuilder(databaseProvider)
}



private fun getDatabaseBuilder(databaseProvider: RoomDatabaseProvider): RoomDatabase.Builder<AppDatabase> {
    check(databaseProvider.getDatabaseName().isNullOrBlank()){
        "Database name is NullOrBlank. Please provide a valid database name by implementing the interface RoomDatabaseProvider and adding it to DI Module"
    }
    val dbFilePath = documentDirectory() + "/" + databaseProvider.getDatabaseName()
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )

    return requireNotNull(documentDirectory?.path)
}