package com.altankoc.themoviesapp.presentation.ui.search

import com.altankoc.themoviesapp.domain.model.Movie

data class SearchScreenState(
    val query: String = "",
    val searchResults: List<Movie> = emptyList(),
    val rawSearchResults: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedGenres: List<String> = emptyList(),
    val availableGenres: List<String> = listOf(
        "Action", "Adventure", "Animation", "Comedy", "Crime",
        "Documentary", "Drama", "Family", "Fantasy", "History",
        "Horror", "Music", "Mystery", "Romance", "Science Fiction",
        "TV Movie", "Thriller", "War", "Western"
    )
)