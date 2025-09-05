package com.altankoc.themoviesapp.data.repository

import com.altankoc.themoviesapp.data.datasource.localdatasource.LocalDataSource
import com.altankoc.themoviesapp.data.datasource.remotedatasource.RemoteDataSource
import com.altankoc.themoviesapp.data.mapper.toDomainModel
import com.altankoc.themoviesapp.data.mapper.toEntity
import com.altankoc.themoviesapp.domain.model.Movie
import com.altankoc.themoviesapp.domain.repository.MovieRepository
import com.altankoc.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MovieRepository {

    private var cachedRandomMovies: List<Movie>? = null

    override fun getPopularMovies(page: Int): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading)
            val response = remoteDataSource.getPopularMovies(page = page)
            if(response.isSuccessful && response.body() != null) {
                val movies = response.body()?.toDomainModel() ?: emptyList()
                emit(Resource.Success(movies))
            } else {
                emit(Resource.Error("An unexpected error occurred"))
            }
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun searchMovies(query: String, page: Int): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading)
            val response = remoteDataSource.searchMovies(
                query = query,
                page = page
            )
            if(response.isSuccessful && response.body() != null) {
                val movies = response.body()?.toDomainModel() ?: emptyList()
                emit(Resource.Success(movies))
            } else {
                emit(Resource.Error("An unexpected error occurred"))
            }
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getRandomMovies(): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading)
            val existing = cachedRandomMovies
            if (existing != null && existing.isNotEmpty()) {
                emit(Resource.Success(existing))
            } else {
                val response = remoteDataSource.getRandomMovies(page = 1)
                if(response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.toDomainModel() ?: emptyList()
                    cachedRandomMovies = movies
                    emit(Resource.Success(movies))
                }
                else {
                    emit(Resource.Error("An unexpected error occurred"))
                }
            }
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun invalidateRandomMoviesCache() {
        cachedRandomMovies = null
    }

    override fun getMovieDetails(movieId: Int): Flow<Resource<Movie>> = flow {
        try {
            emit(Resource.Loading)
            val response = remoteDataSource.getMovieDetails(movieId)
            if(response.isSuccessful) {
                response.body()?.let { movieDto ->
                    val movie = movieDto.toDomainModel()
                    emit(Resource.Success(movie))
                } ?: emit(Resource.Error("Movie not found"))
            } else {
                emit(Resource.Error("Movie not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getFavoriteMovies(): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading)
            localDataSource.getAllFavoriteMovies()
                .map { movieEntities ->
                    val movies = movieEntities.toDomainModel()
                    Resource.Success(movies)
                }
                .collect { emit(it) }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun addToFavorites(movie: Movie): Resource<Unit> {
        return try {
            val movieEntity = movie.toEntity()
            localDataSource.insertMovie(movieEntity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to add to favorites")
        }
    }

    override suspend fun removeFromFavorites(movie: Movie): Resource<Unit> {
        return try {
            val movieEntity = movie.toEntity()
            localDataSource.deleteMovie(movieEntity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to remove from favorites")
        }
    }

    override suspend fun isMovieFavorite(movieId: Int): Resource<Boolean> {
        return try {
            val isFavorite = localDataSource.isMovieFavorite(movieId)
            Resource.Success(isFavorite)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to check favorite status")
        }
    }
}