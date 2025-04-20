package com.example.cinemasinshorts.features.movies.domain.repository

import com.example.cinemasinshorts.features.movies.data.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(): Flow<List<Movie>>
    suspend fun getMovieDetails(movieId: Int): Flow<Movie>
    suspend fun refreshMovies(apiKey:String)

} 