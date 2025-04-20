package com.example.cinemasinshorts.features.movies.data.repository

import com.example.cinemasinshorts.features.movies.data.local.MovieDao
import com.example.cinemasinshorts.features.movies.data.local.MovieEntity
import com.example.cinemasinshorts.features.movies.data.model.Movie
import com.example.cinemasinshorts.features.movies.data.remote.MovieDto
import com.example.cinemasinshorts.features.movies.data.remote.TMDBApiService
import com.example.cinemasinshorts.features.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TMDBApiService,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { it.toMovie() }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Flow<Movie> {
        // Implement movie details fetching
        TODO("Not yet implemented")
    }



    override suspend fun refreshMovies(apiKey: String) {
        try {
            val response = apiService.getPopularMovies(apiKey)
            val movies = response.results.map { it.toMovieEntity() }
            movieDao.deleteAllMovies()
            movieDao.insertMovies(movies)
        } catch (e: Exception) {
            // Handle error
            throw e
        }
    }

    private fun MovieEntity.toMovie() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage
    )

    private fun MovieDto.toMovieEntity() = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = poster_path,
        releaseDate = release_date,
        voteAverage = vote_average
    )
} 