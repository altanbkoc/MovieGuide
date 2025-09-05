package com.altankoc.themoviesapp.data.remote.api

import com.altankoc.themoviesapp.BuildConfig
import com.altankoc.themoviesapp.data.remote.dto.MovieDto
import com.altankoc.themoviesapp.data.remote.dto.MoviesResponseDto
import com.altankoc.themoviesapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}",
        @Query("language") language: String = Constants.LANGUAGE_EN,
        @Query("page") page: Int = 1
    ): Response<MoviesResponseDto>

    @GET("search/movie")
    suspend fun searchMovies(
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}",
        @Query("query") query: String,
        @Query("language") language: String = Constants.LANGUAGE_EN,
        @Query("page") page: Int = 1
    ): Response<MoviesResponseDto>

    @GET("discover/movie")
    suspend fun getRandomMovies(
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("language") language: String = Constants.LANGUAGE_EN,
        @Query("page") page: Int = 1
    ): Response<MoviesResponseDto>


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}",
        @Query("language") language: String = Constants.LANGUAGE_EN
    ): Response<MovieDto>
}