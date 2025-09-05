package com.altankoc.themoviesapp.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.altankoc.themoviesapp.presentation.ui.components.ErrorMessage
import com.altankoc.themoviesapp.presentation.ui.components.LoadingIndicator
import com.altankoc.themoviesapp.presentation.ui.components.MovieItem
import com.altankoc.themoviesapp.presentation.ui.theme.PureWhite

@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoadingPopular || state.isLoadingRandom
    )

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.refresh() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Popular Movies",
                        color = PureWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    when {
                        state.isLoadingPopular -> {
                            LoadingIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                message = "Loading popular movies..."
                            )
                        }
                        state.popularError != null -> {
                            ErrorMessage(
                                message = state.popularError.toString(),
                                onRetryClick = { viewModel.refresh() },
                                modifier = Modifier.height(200.dp)
                            )
                        }
                        else -> {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                items(state.popularMovies) { movie ->
                                    MovieItem(
                                        movie = movie,
                                        onClick = onMovieClick
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Recommended for You",
                    color = PureWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            when {
                state.isLoadingRandom -> {
                    item {
                        LoadingIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            message = "Loading recommendations..."
                        )
                    }
                }
                state.randomError != null -> {
                    item {
                        ErrorMessage(
                            message = state.randomError.toString(),
                            onRetryClick = { viewModel.refresh() },
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }
                else -> {
                    val chunkedMovies = state.randomMovies.chunked(2)
                    items(chunkedMovies) { moviePair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            moviePair.forEach { movie ->
                                MovieItem(
                                    movie = movie,
                                    onClick = onMovieClick,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (moviePair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}