package com.altankoc.themoviesapp.domain.usecase

import com.altankoc.themoviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int) = repository.getMovieDetails(movieId)

}