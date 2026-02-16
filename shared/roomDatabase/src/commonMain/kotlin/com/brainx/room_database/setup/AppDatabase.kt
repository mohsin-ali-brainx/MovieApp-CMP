package com.brainx.room_database.setup

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.brainx.room_database.database.dao.MovieDao
import com.brainx.room_database.database.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
//
//@Database(entities = [Movie::class], version = 1)
//@ConstructedBy(AppDatabaseConstructor::class)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun getMovieDao(): MovieDao
//
//}
//
//fun getMovieDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
//    return builder
//        .setDriver(BundledSQLiteDriver())
//        .setQueryCoroutineContext(Dispatchers.IO)
//        .build()
//}
//
//// The Room compiler generates the `actual` implementations.
//@Suppress("NO_ACTUAL_FOR_EXPECT")
//expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
//    override fun initialize(): AppDatabase
//}


@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun createAppDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .build()
}