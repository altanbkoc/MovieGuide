package com.altankoc.themoviesapp.presentation.ui.home

import com.altankoc.themoviesapp.domain.model.Movie

data class HomeScreenState(
    val popularMovies: List<Movie> = emptyList(),
    val isLoadingPopular: Boolean = false,
    val popularError: String? = null,
    val randomMovies: List<Movie> = emptyList(),
    val isLoadingRandom: Boolean = false,
    val randomError: String? = null
)
