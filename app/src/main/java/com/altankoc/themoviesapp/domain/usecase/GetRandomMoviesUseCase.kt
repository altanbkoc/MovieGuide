package com.altankoc.themoviesapp.domain.usecase

import com.altankoc.themoviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetRandomMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
){
    operator fun invoke() = repository.getRandomMovies()

    fun invalidateCache() = repository.invalidateRandomMoviesCache()
}