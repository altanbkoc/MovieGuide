package com.altankoc.themoviesapp.presentation.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altankoc.themoviesapp.domain.usecase.AddToFavoritesUseCase
import com.altankoc.themoviesapp.domain.usecase.GetMovieDetailsUseCase
import com.altankoc.themoviesapp.domain.usecase.IsMovieFavoriteUseCase
import com.altankoc.themoviesapp.domain.usecase.RemoveFromFavoritesUseCase
import com.altankoc.themoviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailScreenViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val isMovieFavoriteUseCase: IsMovieFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailScreenState())
    val state = _state.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId).collect { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            movie = resource.data,
                            isLoading = false,
                            error = null
                        )
                        checkFavoriteStatus(movieId)
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


    private fun checkFavoriteStatus(movieId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCheckingFavorite = true)
            when(val result = isMovieFavoriteUseCase(movieId)){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isFavorite = result.data,
                        isCheckingFavorite = false
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        isCheckingFavorite = false
                    )

                }

            }
        }
    }

    fun toggleFavorite() {
        val movie = _state.value.movie ?: return
        val isFavorite = _state.value.isFavorite

        viewModelScope.launch {
            _state.value = _state.value.copy(isToggling = true)

            val result = if (isFavorite) {
                removeFromFavoritesUseCase(movie)
            } else {
                addToFavoritesUseCase(movie)
            }

            when(result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isFavorite = !isFavorite,
                        isToggling = false
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        isToggling = false,
                        error = (result as? Resource.Error)?.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}