package com.example.cinemasinshorts.features.movies.data.repository

import com.example.cinemasinshorts.features.movies.data.local.BookmarkedMovie
import com.example.cinemasinshorts.features.movies.data.local.BookmarkedMovieDao
import com.example.cinemasinshorts.features.movies.data.local.MovieDao
import com.example.cinemasinshorts.features.movies.data.local.MovieEntity
import com.example.cinemasinshorts.features.movies.data.model.Movie
import com.example.cinemasinshorts.features.movies.data.remote.Constants
import com.example.cinemasinshorts.features.movies.data.remote.MovieDetailsResponse
import com.example.cinemasinshorts.features.movies.data.remote.MovieDto
import com.example.cinemasinshorts.features.movies.data.remote.TMDBApiService
import com.example.cinemasinshorts.features.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TMDBApiService,
    private val movieDao: MovieDao,
    private val bookmarkedMovieDao: BookmarkedMovieDao
) : MovieRepository {

    override suspend fun getMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { it.toMovie() }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse {
        return apiService.getMovieDetails(movieId, Constants.TMDB_API_KEY)
    }

    override fun getAllBookmarkedMovies(): Flow<List<BookmarkedMovie>> {
        return bookmarkedMovieDao.getAllBookmarkedMovies()
    }

    override suspend fun insertBookmarkedMovie(movie: BookmarkedMovie) {
        bookmarkedMovieDao.insertBookmarkedMovie(movie)
    }

    override suspend fun deleteBookmarkedMovie(movie: BookmarkedMovie) {
        bookmarkedMovieDao.deleteBookmarkedMovie(movie)
    }

    override suspend fun getBookmarkedMovie(movieId: Int): BookmarkedMovie? {
        return bookmarkedMovieDao.getBookmarkedMovie(movieId)
    }

    override suspend fun refreshMovies(apiKey: String) {
        val response = apiService.getPopularMovies(apiKey)
        val movies = response.results.map { dto ->
            MovieEntity(
                id = dto.id,
                title = dto.title,
                overview = dto.overview,
                posterPath = dto.poster_path,
                releaseDate = dto.release_date,
                voteAverage = dto.vote_average
            )
        }
        movieDao.insertMovies(movies)
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