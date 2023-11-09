package com.example.start2.home.navigators


sealed class RootScreen(val route: String) {
    object Home : RootScreen("home_root")
    object Search : RootScreen("search_root")
    object Favorites : RootScreen("favorites_root")
    object Profile : RootScreen("profile_root")
}

sealed class LeafScreen(val route: String) {
    object Home : LeafScreen("home")
    object Search : LeafScreen("search")
    object Favorites : LeafScreen("favorites")
    object Profile : LeafScreen("profile")
    object HomeDetail : LeafScreen("home_detail")
    object BookReader : LeafScreen("book_reader")
}