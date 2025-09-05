package com.altankoc.themoviesapp.presentation.ui.moviedetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.altankoc.themoviesapp.presentation.ui.components.ErrorMessage
import com.altankoc.themoviesapp.presentation.ui.components.LoadingIndicator
import com.altankoc.themoviesapp.presentation.ui.theme.*
import com.altankoc.themoviesapp.util.GenreMapping
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("ConfigurationScreenWidthHeight", "DefaultLocale")
@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBackClick: () -> Unit,
    viewModel: MovieDetailScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val imageHeight = (configuration.screenHeightDp * 0.35f).dp

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    when {
        state.isLoading -> {
            LoadingIndicator(
                modifier = Modifier.fillMaxSize(),
                message = "Loading movie details..."
            )
        }
        state.error != null -> {
            ErrorMessage(
                message = state.error.toString(),
                onRetryClick = { viewModel.loadMovieDetails(movieId) },
                modifier = Modifier.fillMaxSize()
            )
        }
        state.movie != null -> {
            val movie = state.movie

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie?.backdropUrl?.ifEmpty { movie.posterUrl })
                            .crossfade(true)
                            .build(),
                        contentDescription = movie?.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        DeepBlack.copy(alpha = 0.8f)
                                    ),
                                    startY = 200f
                                )
                            )
                    )
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = DeepBlack.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = PureWhite
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(
                                DeepBlack.copy(alpha = 0.8f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = RatingGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", movie?.voteAverage),
                            color = PureWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = movie!!.title,
                            color = PureWhite,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (state.isFavorite) CinemaRed else LightGray,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { viewModel.toggleFavorite() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    if (movie!!.originalTitle != movie.title) {
                        Text(
                            text = "Original: ${movie.originalTitle}",
                            color = LightGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Released: ${formatReleaseDate(movie.releaseDate)}",
                            color = LightGray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Language: ${languageDisplayName(movie.originalLanguage)}",
                            color = LightGray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    val genres = movie.genreIds.mapNotNull { GenreMapping.GENRE_MAP[it] }
                    if (genres.isNotEmpty()) {
                        Text(
                            text = "Genres",
                            color = PureWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            genres.take(3).forEach { genre ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = CinemaRed.copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                                ) {
                                    Text(
                                        text = genre,
                                        color = CinemaRed,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Text(
                        text = "Overview",
                        color = PureWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = movie.overview.ifEmpty { "No overview available." },
                        color = LightGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Details",
                                color = PureWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Votes",
                                        color = LightGray,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = NumberFormat.getIntegerInstance(Locale.getDefault()).format(movie.voteCount),
                                        color = PureWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Popularity",
                                        color = LightGray,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = String.format("%.1f", movie.popularity),
                                        color = PureWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Column {
                                    Text(
                                        text = "18+",
                                        color = LightGray,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = if (movie.adult) "Yes" else "No",
                                        color = PureWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatReleaseDate(dateStr: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }
        val date = parser.parse(dateStr)
        if (date != null) {
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            formatter.format(date)
        } else {
            dateStr
        }
    } catch (e: Exception) {
        dateStr
    }
}

private fun languageDisplayName(code: String): String {
    return try {
        val locale = Locale(code)
        val display = locale.getDisplayLanguage(Locale.ENGLISH)
        if (display.isNullOrBlank()) code.uppercase(Locale.ENGLISH) else display.replaceFirstChar { it.titlecase(Locale.ENGLISH) }
    } catch (e: Exception) {
        code.uppercase(Locale.ENGLISH)
    }
}