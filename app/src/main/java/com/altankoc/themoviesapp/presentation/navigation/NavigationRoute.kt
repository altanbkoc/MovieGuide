package com.altankoc.themoviesapp.presentation.navigation

sealed class NavigationRoute(val route: String) {
    object Splash: NavigationRoute("splash")
    object Home: NavigationRoute("home")
    object Search: NavigationRoute("search")
    object Favorites: NavigationRoute("favorites")
    object MovieDetail: NavigationRoute("movie_detail/{movieId}") {
        fun createRoute(movieId: Int) = "movie_detail/$movieId"
    }
}