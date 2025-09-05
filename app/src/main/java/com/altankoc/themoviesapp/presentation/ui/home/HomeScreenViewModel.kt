package com.altankoc.themoviesapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altankoc.themoviesapp.domain.usecase.GetPopularMoviesUseCase
import com.altankoc.themoviesapp.domain.usecase.GetRandomMoviesUseCase
import com.altankoc.themoviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getRandomMoviesUseCase: GetRandomMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    init {
        loadPopularMovies()
        loadRandomMovies()
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            getPopularMoviesUseCase().collect { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoadingPopular = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            popularMovies = resource.data,
                            isLoadingPopular = false,
                            popularError = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoadingPopular = false,
                            popularError = resource.message
                        )
                    }
                }
            }
        }
    }

    private fun loadRandomMovies() {
        viewModelScope.launch {
            getRandomMoviesUseCase().collect { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoadingRandom = true
                        )
                    }
                    is Resource.Success -> {
                        val filtered = resource.data.filter { it.voteAverage > 6.0 }
                        _state.value = _state.value.copy(
                            randomMovies = filtered,
                            isLoadingRandom = false,
                            randomError = null
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoadingRandom = false,
                            randomError = resource.message
                        )
                    }
                }
            }
        }
    }

    fun refresh() {
        loadPopularMovies()
        getRandomMoviesUseCase.invalidateCache()
        loadRandomMovies()
    }
}