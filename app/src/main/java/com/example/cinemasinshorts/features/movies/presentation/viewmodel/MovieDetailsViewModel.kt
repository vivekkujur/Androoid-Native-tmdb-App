package com.example.cinemasinshorts.features.movies.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemasinshorts.features.movies.data.local.BookmarkedMovie
import com.example.cinemasinshorts.features.movies.data.local.BookmarkedMovieDao
import com.example.cinemasinshorts.features.movies.data.remote.Constants
import com.example.cinemasinshorts.features.movies.data.remote.TMDBApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movieDetails: MovieDetails = MovieDetails()
)

data class MovieDetails(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val releaseDate: String = "",
    val voteAverage: Double = 0.0,
    val runtime: Int = 0,
    val genres: String = "",
    val backdropPath: String = "",
    val isBookmarked: Boolean = false
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val apiService: TMDBApiService,
    private val bookmarkedMovieDao: BookmarkedMovieDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsState())
    val state: StateFlow<MovieDetailsState> = _state.asStateFlow()

    private val movieId: Int = savedStateHandle.get<Int>("movieId") ?: 0

    init {
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                
                // Check if movie is bookmarked
                val bookmarkedMovie = bookmarkedMovieDao.getBookmarkedMovie(movieId)
                
                // Fetch movie details from API
                val movieDetails = apiService.getMovieDetails(movieId, Constants.TMDB_API_KEY)
                
                // Convert to UI state
                val movieDetailsState = MovieDetails(
                    id = movieDetails.id,
                    title = movieDetails.title,
                    overview = movieDetails.overview,
                    posterPath = movieDetails.poster_path,
                    releaseDate = movieDetails.release_date,
                    voteAverage = movieDetails.vote_average,
                    runtime = movieDetails.runtime,
                    genres = movieDetails.genres.joinToString { it.name },
                    backdropPath = movieDetails.backdrop_path,
                    isBookmarked = bookmarkedMovie != null
                )
                
                _state.value = _state.value.copy(
                    isLoading = false,
                    movieDetails = movieDetailsState
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            try {
                val currentState = _state.value
                val movieDetails = currentState.movieDetails
                
                if (movieDetails.isBookmarked) {
                    // Remove from bookmarks
                    bookmarkedMovieDao.deleteBookmarkedMovie(
                        BookmarkedMovie(
                            id = movieDetails.id,
                            title = movieDetails.title,
                            overview = movieDetails.overview,
                            posterPath = movieDetails.posterPath,
                            releaseDate = movieDetails.releaseDate,
                            voteAverage = movieDetails.voteAverage,
                            runtime = movieDetails.runtime,
                            genres = movieDetails.genres,
                            backdropPath = movieDetails.backdropPath
                        )
                    )
                } else {
                    // Add to bookmarks
                    bookmarkedMovieDao.insertBookmarkedMovie(
                        BookmarkedMovie(
                            id = movieDetails.id,
                            title = movieDetails.title,
                            overview = movieDetails.overview,
                            posterPath = movieDetails.posterPath,
                            releaseDate = movieDetails.releaseDate,
                            voteAverage = movieDetails.voteAverage,
                            runtime = movieDetails.runtime,
                            genres = movieDetails.genres,
                            backdropPath = movieDetails.backdropPath
                        )
                    )
                }
                
                // Update UI state
                _state.value = currentState.copy(
                    movieDetails = movieDetails.copy(
                        isBookmarked = !movieDetails.isBookmarked
                    )
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to update bookmark"
                )
            }
        }
    }
} 