package com.altankoc.themoviesapp.presentation.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.altankoc.themoviesapp.presentation.ui.components.ErrorMessage
import com.altankoc.themoviesapp.presentation.ui.components.LoadingIndicator
import com.altankoc.themoviesapp.presentation.ui.components.MovieItem
import com.altankoc.themoviesapp.presentation.ui.theme.*

@Composable
fun FavoriteScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: FavoriteScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Favorites",
            color = PureWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            state.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    message = "Loading your favorites..."
                )
            }
            state.error != null -> {
                ErrorMessage(
                    message = state.error.toString(),
                    onRetryClick = { viewModel.refresh() },
                    modifier = Modifier.weight(1f)
                )
            }
            state.favoriteMovies.isEmpty() -> {
                EmptyFavoritesState(
                    modifier = Modifier.weight(1f)
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val chunkedMovies = state.favoriteMovies.chunked(2)
                    items(chunkedMovies) { moviePair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            moviePair.forEach { movie ->
                                Box(modifier = Modifier.weight(1f)) {
                                    MovieItem(
                                        movie = movie,
                                        onClick = onMovieClick,
                                        trailingContent = {
                                            val context = LocalContext.current
                                            var showDialog by remember { mutableStateOf(false) }

                                            IconButton(onClick = { showDialog = true }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Delete,
                                                    contentDescription = "Remove from favorites",
                                                    tint = CinemaRed
                                                )
                                            }

                                            if (showDialog) {
                                                AlertDialog(
                                                    onDismissRequest = { showDialog = false },
                                                    confirmButton = {
                                                        TextButton(onClick = {
                                                            showDialog = false
                                                            viewModel.removeFromFavorites(movie)
                                                            android.widget.Toast.makeText(
                                                                context,
                                                                "Removed from favorites",
                                                                android.widget.Toast.LENGTH_SHORT
                                                            ).show()
                                                        }) {
                                                            Text("Yes")
                                                        }
                                                    },
                                                    dismissButton = {
                                                        TextButton(onClick = { showDialog = false }) {
                                                            Text("No")
                                                        }
                                                    },
                                                    title = { Text("Remove from favorites?") },
                                                    text = { Text("Are you sure you want to remove this item from favorites?") }
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                            if (moviePair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }


        state.error?.let { error ->
            LaunchedEffect(error) {
                kotlinx.coroutines.delay(3000)
                viewModel.clearError()
            }
        }
    }
}

@Composable
fun EmptyFavoritesState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎬",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "No favorite movies yet",
            color = PureWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Start exploring and add movies to your favorites!",
            color = LightGray,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}