package com.altankoc.themoviesapp.presentation.ui.moviedetail

import com.altankoc.themoviesapp.domain.model.Movie

data class MovieDetailScreenState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isCheckingFavorite: Boolean = false,
    val isToggling: Boolean = false
)
