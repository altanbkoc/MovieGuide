package com.altankoc.themoviesapp.data.remote.dto

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double,
    val vote_count: Int,
    val popularity: Double,
    val adult: Boolean,
    val genre_ids: List<Int>?,
    val genres: List<GenreItemDto>?,
    val original_language: String,
    val original_title: String,
    val video: Boolean
)

data class GenreItemDto(
    val id: Int,
    val name: String
)
