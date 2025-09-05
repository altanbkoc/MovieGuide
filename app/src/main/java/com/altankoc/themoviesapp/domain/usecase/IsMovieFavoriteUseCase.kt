package com.altankoc.themoviesapp.domain.usecase

import com.altankoc.themoviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class IsMovieFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.isMovieFavorite(movieId)

}