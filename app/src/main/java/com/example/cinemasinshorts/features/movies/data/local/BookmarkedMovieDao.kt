package com.example.cinemasinshorts.features.movies.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedMovieDao {
    @Query("SELECT * FROM bookmarked_movies")
    fun getAllBookmarkedMovies(): Flow<List<BookmarkedMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkedMovie(movie: BookmarkedMovie)

    @Delete
    suspend fun deleteBookmarkedMovie(movie: BookmarkedMovie)

    @Query("SELECT * FROM bookmarked_movies WHERE id = :movieId")
    suspend fun getBookmarkedMovie(movieId: Int): BookmarkedMovie?
} 