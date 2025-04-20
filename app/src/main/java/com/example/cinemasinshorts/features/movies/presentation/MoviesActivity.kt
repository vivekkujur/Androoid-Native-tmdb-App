package com.example.cinemasinshorts.features.movies.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cinemasinshorts.R
import com.example.cinemasinshorts.base.BaseActivity
import com.example.cinemasinshorts.databinding.ActivityMoviesBinding
import com.example.cinemasinshorts.features.movies.data.model.Movie
import com.example.cinemasinshorts.features.movies.presentation.adapter.MoviesAdapter
import com.example.cinemasinshorts.features.movies.presentation.viewmodel.MoviesEvent
import com.example.cinemasinshorts.features.movies.presentation.viewmodel.MoviesState
import com.example.cinemasinshorts.features.movies.presentation.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesActivity : BaseActivity<ActivityMoviesBinding>() {

    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var moviesAdapter: MoviesAdapter

    override fun getLayoutId(): Int = R.layout.activity_movies

    override fun setupViews() {
        binding.viewModel = viewModel
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter { movie ->
            // Handle bookmark click
            // TODO: Implement bookmark functionality
        }
        binding.rvMovies.adapter = moviesAdapter
    }

    override fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state?.let { handleState(it) }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    event?.let { handleEvent(it) }
                }
            }
        }
    }

    private fun handleState(state: MoviesState) {
        if (!state.isLoading && state.error == null) {
            moviesAdapter.submitList(state.movies)
        }
    }

    private fun handleEvent(event: MoviesEvent) {
        when (event) {
            is MoviesEvent.ShowError -> {
                // Show error message
                // TODO: Implement error handling
            }
        }
    }
} 