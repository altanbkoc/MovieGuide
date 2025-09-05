package com.altankoc.themoviesapp.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val CinemaColorScheme = darkColorScheme(
    primary = CinemaRed,
    secondary = RatingGold,
    tertiary = SoftGray,
    background = DeepBlack,
    surface = SurfaceGray,
    surfaceVariant = SoftGray,
    onPrimary = PureWhite,
    onSecondary = DeepBlack,
    onTertiary = PureWhite,
    onBackground = PureWhite,
    onSurface = PureWhite,
    onSurfaceVariant = LightGray,
    outline = DisabledGray,
    outlineVariant = DisabledGray
)

@Composable
fun TheMoviesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            CinemaColorScheme
        }
        darkTheme -> CinemaColorScheme
        else -> CinemaColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}