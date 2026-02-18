package com.brainx.room_database.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
)