package com.example.cinemasinshorts.features.movies.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_movies")
data class BookmarkedMovie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val runtime: Int,
    val genres: String,
    val backdropPath: String
) 