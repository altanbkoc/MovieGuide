package com.altankoc.themoviesapp.data.remote.dto

data class MoviesResponseDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)