package com.altankoc.themoviesapp.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altankoc.themoviesapp.domain.model.Movie
import com.altankoc.themoviesapp.domain.usecase.SearchMoviesUseCase
import com.altankoc.themoviesapp.util.GenreMapping
import com.altankoc.themoviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchScreenState())
    val state = _state.asStateFlow()

    fun updateQuery(query: String) {
        _state.value = _state.value.copy(query = query)
        if(query.isNotBlank()) {
            searchMovies(query)
        } else {
            _state.value = _state.value.copy(
                searchResults = emptyList(),
                rawSearchResults = emptyList()
            )
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            searchMoviesUseCase(query).collect { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(rawSearchResults = resource.data)
                        val filteredMovies = filterMoviesByGenres(resource.data)
                        _state.value = _state.value.copy(
                            searchResults = filteredMovies,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }
                }
            }
        }
    }

    private fun filterMoviesByGenres(movies: List<Movie>): List<Movie> {
        val selectedGenres = _state.value.selectedGenres
        if(selectedGenres.isEmpty()) return movies

        return movies.filter { movie ->
            val movieGenres = movie.genreIds.mapNotNull { genreId ->
                GenreMapping.GENRE_MAP[genreId]
            }
            selectedGenres.any { selectedGenre ->
                movieGenres.contains(selectedGenre)
            }
        }
    }

    fun toggleGenre(genre: String) {
        val currentGenres = _state.value.selectedGenres
        val newGenres = if(currentGenres.contains(genre)){
            currentGenres - genre
        } else {
            currentGenres + genre
        }
        _state.value = _state.value.copy(selectedGenres = newGenres)

        val filteredResults = filterMoviesByGenres(_state.value.rawSearchResults)
        _state.value = _state.value.copy(searchResults = filteredResults)
    }
}