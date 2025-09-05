package com.altankoc.themoviesapp.domain.repository

import com.altankoc.themoviesapp.domain.model.Movie
import com.altankoc.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    //Remote
    fun getPopularMovies(page: Int = 1): Flow<Resource<List<Movie>>>

    fun searchMovies(query: String, page: Int = 1): Flow<Resource<List<Movie>>>

    fun getRandomMovies(): Flow<Resource<List<Movie>>>

    fun invalidateRandomMoviesCache()

    fun getMovieDetails(movieId: Int): Flow<Resource<Movie>>

    //Local
    fun getFavoriteMovies(): Flow<Resource<List<Movie>>>

    suspend fun addToFavorites(movie: Movie): Resource<Unit>

    suspend fun removeFromFavorites(movie: Movie): Resource<Unit>

    suspend fun isMovieFavorite(movieId: Int): Resource<Boolean>
}