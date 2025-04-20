package com.example.cinemasinshorts.features.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, BookmarkedMovie::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun bookmarkedMovieDao(): BookmarkedMovieDao
} 