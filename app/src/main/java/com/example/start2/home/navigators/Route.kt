package com.example.start2.home.navigators


sealed class RootScreen(val route: String) {
    object Home : RootScreen("home_root")
    object Search : RootScreen("search_root")
    object Favorites : RootScreen("favorites_root")
    object Profile : RootScreen("profile_root")
    object Rate : RootScreen("rate_root")
    object Analysis : RootScreen("analysis_root")
}

sealed class LeafScreen(val route: String) {
    object Home : LeafScreen("home")
    object Search : LeafScreen("search")
    object HomeDetail : LeafScreen("home_detail")
    object Favorites : LeafScreen("favorites")
    object Rate : LeafScreen("rate")
    object Profile : LeafScreen("profile")
    object Followers : LeafScreen("followers")
    object AnalysisChart : LeafScreen("analysis_chart")
    data class Friend(val username: String) :LeafScreen("friend/{username}")
    //Analysis table is noted as analysis for naming conventions
    object Analysis : LeafScreen("analysis")
}