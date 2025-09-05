package com.altankoc.themoviesapp.data.datasource.localdatasource

import com.altankoc.themoviesapp.data.local.dao.MovieDao
import com.altankoc.themoviesapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao
) : LocalDataSource {

    override fun getAllFavoriteMovies(): Flow<List<MovieEntity>> {
        return movieDao.getAllFavoritesMovies()
    }

    override suspend fun insertMovie(movie: MovieEntity) {
        return movieDao.insertMovie(movie)
    }

    override suspend fun deleteMovie(movie: MovieEntity) {
        return movieDao.deleteMovie(movie)
    }

    override suspend fun isMovieFavorite(movieId: Int): Boolean {
        return movieDao.isMovieFavorite(movieId)
    }
}