package com.altankoc.themoviesapp.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altankoc.themoviesapp.domain.model.Movie
import com.altankoc.themoviesapp.domain.usecase.GetFavoriteMoviesUseCase
import com.altankoc.themoviesapp.domain.usecase.RemoveFromFavoritesUseCase
import com.altankoc.themoviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteScreenState())
    val state = _state.asStateFlow()

    init {
        loadFavoriteMovies()
    }

    private fun loadFavoriteMovies() {
        viewModelScope.launch {
            getFavoriteMoviesUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            favoriteMovies = resource.data,
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

    fun removeFromFavorites(movie: Movie) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRemoving = true)
            when(val result = removeFromFavoritesUseCase(movie)){
                is Resource.Success -> {
                    _state.value = _state.value.copy(isRemoving = false)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isRemoving = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {

                }
            }
        }
    }
    fun refresh() {
        loadFavoriteMovies()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

}