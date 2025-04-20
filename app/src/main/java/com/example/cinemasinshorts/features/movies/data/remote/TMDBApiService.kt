package com.example.cinemasinshorts.features.movies.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailsResponse
}

data class MovieResponse(
    val results: List<MovieDto>
)

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double
)

//data class MovieDetailsResponse(
//    val id: Int,
//    val title: String,
//    val overview: String,
//    val poster_path: String,
//    val release_date: String,
//    val vote_average: Double,
//    val runtime: Int,
//    val genres: List<GenreDto>,
//    val backdrop_path: String
//)

data class GenreDto(
    val id: Int,
    val name: String
) 