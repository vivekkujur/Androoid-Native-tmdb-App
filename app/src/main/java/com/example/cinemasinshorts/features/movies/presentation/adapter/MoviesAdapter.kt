package com.example.cinemasinshorts.features.movies.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemasinshorts.R
import com.example.cinemasinshorts.databinding.ItemMovieBinding
import com.example.cinemasinshorts.features.movies.data.model.Movie
import com.example.cinemasinshorts.features.movies.data.remote.Constants
import com.example.cinemasinshorts.features.movies.presentation.MovieDetailsActivity

class MoviesAdapter(
    private val onBookmarkClick: (Movie) -> Unit
) : ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding, onBookmarkClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onBookmarkClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val movie = binding.movie
                movie?.let {
                    val intent = Intent(binding.root.context, MovieDetailsActivity::class.java).apply {
                        putExtra("movieId", it.id)
                    }
                    binding.root.context.startActivity(intent)
                }
            }

            binding.ivBookmark.setOnClickListener {
                val movie = binding.movie
                movie?.let { onBookmarkClick(it) }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                this.movie = movie
                Glide.with(ivMoviePoster)
                    .load("${Constants.TMDB_IMAGE_BASE_URL}${Constants.ImageSizes.POSTER}${movie.posterPath}")
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(ivMoviePoster)
            }
        }
    }

    private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
} 