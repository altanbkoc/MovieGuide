package com.altankoc.themoviesapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.altankoc.themoviesapp.presentation.ui.favorites.FavoriteScreen
import com.altankoc.themoviesapp.presentation.ui.home.HomeScreen
import com.altankoc.themoviesapp.presentation.ui.moviedetail.MovieDetailScreen
import com.altankoc.themoviesapp.presentation.ui.search.SearchScreen
import com.altankoc.themoviesapp.presentation.ui.splash.SplashScreen
import com.altankoc.themoviesapp.presentation.ui.theme.DeepBlack

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieNavigation(
    navController: NavHostController = rememberNavController()
) {
    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, NavigationRoute.Home.route),
        BottomNavItem("Search", Icons.Default.Search, NavigationRoute.Search.route),
        BottomNavItem("Favorites", Icons.Default.Favorite, NavigationRoute.Favorites.route)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }


    val navBarBrush = Brush.horizontalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF2B2B2B),
            0.5f to DeepBlack,
            1.0f to Color(0xFF2B2B2B)
        )
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.background(navBarBrush),
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                indicatorColor = Color.Transparent
                            ),
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(NavigationRoute.Splash.route) {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(NavigationRoute.Home.route) {
                            popUpTo(NavigationRoute.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(NavigationRoute.Home.route) {
                HomeScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(NavigationRoute.MovieDetail.createRoute(movieId))
                    }
                )
            }

            composable(NavigationRoute.Search.route) {
                SearchScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(NavigationRoute.MovieDetail.createRoute(movieId))
                    }
                )
            }

            composable(NavigationRoute.Favorites.route) {
                FavoriteScreen(
                    onMovieClick = { movieId ->
                        navController.navigate(NavigationRoute.MovieDetail.createRoute(movieId))
                    }
                )
            }

            composable(NavigationRoute.MovieDetail.route) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: 0
                MovieDetailScreen(
                    movieId = movieId,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }
}