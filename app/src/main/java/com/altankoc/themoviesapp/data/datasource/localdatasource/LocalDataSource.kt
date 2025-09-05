package com.altankoc.themoviesapp.data.datasource.localdatasource

import com.altankoc.themoviesapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getAllFavoriteMovies(): Flow<List<MovieEntity>>

    suspend fun insertMovie(movie: MovieEntity)

    suspend fun deleteMovie(movie: MovieEntity)

    suspend fun isMovieFavorite(movieId: Int): Boolean
}