package com.altankoc.themoviesapp.data.datasource.remotedatasource

import com.altankoc.themoviesapp.data.remote.dto.MovieDto
import com.altankoc.themoviesapp.data.remote.dto.MoviesResponseDto
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getPopularMovies(page: Int): Response<MoviesResponseDto>

    suspend fun searchMovies(query: String, page: Int): Response<MoviesResponseDto>

    suspend fun getRandomMovies(page: Int): Response<MoviesResponseDto>

    suspend fun getMovieDetails(movieId: Int): Response<MovieDto>

}