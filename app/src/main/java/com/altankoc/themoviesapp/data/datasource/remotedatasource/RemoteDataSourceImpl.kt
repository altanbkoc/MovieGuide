package com.altankoc.themoviesapp.data.datasource.remotedatasource

import com.altankoc.themoviesapp.data.remote.api.ApiService
import com.altankoc.themoviesapp.data.remote.dto.MovieDto
import com.altankoc.themoviesapp.data.remote.dto.MoviesResponseDto
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun getPopularMovies(page: Int): Response<MoviesResponseDto> {
        return apiService.getPopularMovies(page = page)
    }

    override suspend fun searchMovies(query: String, page: Int): Response<MoviesResponseDto> {
        return apiService.searchMovies(
            query = query,
            page = page
        )
    }

    override suspend fun getRandomMovies(page: Int): Response<MoviesResponseDto> {
        val randomPage = (1..500).random()
        return apiService.getRandomMovies(page = randomPage)
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDto> {
        return apiService.getMovieDetails(movieId = movieId)
    }
}