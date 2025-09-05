package com.altankoc.themoviesapp.domain.usecase

import com.altankoc.themoviesapp.domain.repository.MovieRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(query: String, page: Int = 1) = repository.searchMovies(query, page)

}