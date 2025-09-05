package com.altankoc.themoviesapp.presentation.ui.favorites

import com.altankoc.themoviesapp.domain.model.Movie

data class FavoriteScreenState(
    val favoriteMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRemoving: Boolean = false
)
