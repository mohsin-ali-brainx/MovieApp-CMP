package com.brainx.room_database.setup

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.brainx.room_database.utils.DatabaseConstants

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun createDatabase(): RoomDatabase.Builder<AppDatabase>  = getDatabaseBuilder(context)
}

private fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.applicationContext.getDatabasePath(DatabaseConstants.DATABASE_NAME)

    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath,
    )
}