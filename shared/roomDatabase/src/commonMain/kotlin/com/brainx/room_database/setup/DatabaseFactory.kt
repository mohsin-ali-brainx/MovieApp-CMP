package com.brainx.room_database.setup

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun createDatabase(): RoomDatabase.Builder<AppDatabase>
}