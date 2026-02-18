package com.brainx.room_database.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.brainx.room_database.database.entity.Movie
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getMovies(): Flow<List<Movie>>

    @Insert
    suspend fun insert(movie: Movie)

    @Query("DELETE FROM movie")
    suspend fun deleteMovies()
}