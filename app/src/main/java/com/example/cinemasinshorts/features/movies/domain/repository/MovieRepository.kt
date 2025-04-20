package com.example.cinemasinshorts.features.movies.domain.repository

import com.example.cinemasinshorts.features.movies.data.local.BookmarkedMovie
import com.example.cinemasinshorts.features.movies.data.model.Movie
import com.example.cinemasinshorts.features.movies.data.remote.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse
    suspend fun refreshMovies(apiKey:String)
    fun getAllBookmarkedMovies(): Flow<List<BookmarkedMovie>>
    suspend fun insertBookmarkedMovie(movie: BookmarkedMovie)
    suspend fun deleteBookmarkedMovie(movie: BookmarkedMovie)
    suspend fun getBookmarkedMovie(movieId: Int): BookmarkedMovie?
} 