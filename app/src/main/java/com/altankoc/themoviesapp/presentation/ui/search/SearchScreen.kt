package com.altankoc.themoviesapp.presentation.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.altankoc.themoviesapp.presentation.ui.components.ErrorMessage
import com.altankoc.themoviesapp.presentation.ui.components.LoadingIndicator
import com.altankoc.themoviesapp.presentation.ui.components.MovieItem
import com.altankoc.themoviesapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { viewModel.updateQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search movies...", color = LightGray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = LightGray
                )
            },
            trailingIcon = {
                if (state.query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = LightGray
                        )
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CinemaRed,
                unfocusedBorderColor = SoftGray,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite,
                cursorColor = CinemaRed
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Genres",
            color = PureWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(state.availableGenres) { genre ->
                GenreChip(
                    genre = genre,
                    isSelected = genre in state.selectedGenres,
                    onClick = { viewModel.toggleGenre(genre) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        when {
            state.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    message = "Searching movies..."
                )
            }
            state.error != null -> {
                ErrorMessage(
                    message = state.error.toString(),
                    onRetryClick = { viewModel.updateQuery(state.query) },
                    modifier = Modifier.weight(1f)
                )
            }
            state.searchResults.isEmpty() && state.query.isNotBlank() -> {
                EmptyState(
                    message = "No movies found for \"${state.query}\"",
                    modifier = Modifier.weight(1f)
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val chunkedMovies = state.searchResults.chunked(2)
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

@Composable
fun GenreChip(
    genre: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        onClick = onClick,
        label = { Text(genre) },
        selected = isSelected,
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = CinemaRed,
            selectedLabelColor = PureWhite,
            containerColor = SoftGray,
            labelColor = LightGray
        )
    )
}

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = message,
            color = LightGray,
            fontSize = 16.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}