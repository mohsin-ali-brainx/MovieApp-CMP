package com.brainx.room_database.setup

import androidx.room.RoomDatabase

internal expect class DatabaseFactory {
    fun createDatabase(): RoomDatabase.Builder<AppDatabase>
}