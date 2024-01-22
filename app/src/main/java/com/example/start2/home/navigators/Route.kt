package com.example.start2.home.navigators


sealed class RootScreen(val route: String) {
    object Home : RootScreen("home_root")
    object Search : RootScreen("search_root")
    object Recommendation : RootScreen("recommendation_root")
    object Profile : RootScreen("profile_root")
    object Rate : RootScreen("rate_root")
    object Analysis : RootScreen("analysis_root")
}

sealed class LeafScreen(val route: String) {

    data class Friend(val username: String) : LeafScreen("friend/{username}")
    object Home : LeafScreen("home")
    object Search : LeafScreen("search")
    object HomeDetail : LeafScreen("home_detail")
    object Recommendation : LeafScreen("recommendation")
    object Rate : LeafScreen("rate")
    object Profile : LeafScreen("profile")
    object Followers : LeafScreen("followers")
    object AnalysisTable : LeafScreen("analysis_table")
    //Analysis table is noted as analysis for naming conventions
    object Analysis : LeafScreen("analysis")

    object SongInfo : LeafScreen("song_info")

    object ArtistInfo: LeafScreen("artist_info")

    object FriendScreen: LeafScreen("friendscreen")

    object AlbumInfo : LeafScreen("album_info")

    object UserGroupScreen: LeafScreen("groupscreen")

}