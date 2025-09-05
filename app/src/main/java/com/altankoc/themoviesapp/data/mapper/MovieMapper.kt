package com.altankoc.themoviesapp.data.mapper

import com.altankoc.themoviesapp.data.local.entity.MovieEntity
import com.altankoc.themoviesapp.data.remote.dto.MovieDto
import com.altankoc.themoviesapp.data.remote.dto.MoviesResponseDto
import com.altankoc.themoviesapp.domain.model.Movie
import com.altankoc.themoviesapp.util.Constants.IMAGE_BASE_URL


fun MovieDto.toDomainModel() : Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview ?: "",
        posterUrl = if (this.poster_path != null) {
            IMAGE_BASE_URL + this.poster_path
        } else "",
        backdropUrl = if (this.backdrop_path != null) {
            IMAGE_BASE_URL + this.backdrop_path
        } else "",
        releaseDate = this.release_date ?: "",
        voteAverage = this.vote_average,
        voteCount = this.vote_count,
        popularity = this.popularity,
        adult = this.adult,
        genreIds = when {
            this.genre_ids != null -> this.genre_ids
            this.genres != null -> this.genres.map { it.id }
            else -> emptyList()
        },
        originalLanguage = this.original_language,
        originalTitle = this.original_title,
        video = this.video
    )
}

fun MoviesResponseDto.toDomainModel() : List<Movie> {
    return this.results.map { it.toDomainModel() }
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterUrl,
        backdropUrl = this.backdropUrl,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        popularity = this.popularity,
        adult = this.adult,
        genreIds = this.genreIds.joinToString(","),
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        video = this.video
    )
}


fun MovieEntity.toDomainModel(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterUrl,
        backdropUrl = this.backdropUrl,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        popularity = this.popularity,
        adult = this.adult,
        genreIds = this.genreIds.split(",").mapNotNull { it.toIntOrNull() },
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        video = this.video
    )
}

fun List<MovieEntity>.toDomainModel(): List<Movie> {
    return this.map { it.toDomainModel() }
}