package com.altankoc.themoviesapp.domain.usecase

import com.altankoc.themoviesapp.domain.model.Movie
import com.altankoc.themoviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) = repository.addToFavorites(movie)

}