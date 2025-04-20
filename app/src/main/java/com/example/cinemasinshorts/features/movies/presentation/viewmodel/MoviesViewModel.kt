package com.example.cinemasinshorts.features.movies.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.cinemasinshorts.base.BaseViewModel
import com.example.cinemasinshorts.features.movies.data.model.Movie
import com.example.cinemasinshorts.features.movies.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoviesState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

sealed class MoviesEvent {
    data class ShowError(val message: String) : MoviesEvent()
}

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel<MoviesState, MoviesEvent>() {

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                setState(MoviesState(isLoading = true))
                repository.refreshMovies("42c6e41563c3159135682b58ae749904")
                repository.getMovies().collect { movies ->
                    setState(MoviesState(movies = movies))
                }
            } catch (e: Exception) {
                setState(MoviesState(error = e.message))
                setEvent(MoviesEvent.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
} 