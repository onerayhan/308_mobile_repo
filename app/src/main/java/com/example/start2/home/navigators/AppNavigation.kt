package com.example.start2.home.navigators

import AnalysisScreen
import FollowersScreen
import SingerScreen
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.start2.home.ProfileScreen


import com.example.start2.home.screens.AnalysisTableScreen

import com.example.start2.home.screens.FriendScreen
import com.example.start2.home.screens.HomeDetailScreen
import com.example.start2.home.screens.HomeScreen
import com.example.start2.home.screens.RateScreen
import com.example.start2.home.screens.RecommendationScreen
import com.example.start2.home.screens.SearchScreen
import com.example.start2.home.screens.info_screens.AlbumInfoScreen
import com.example.start2.home.screens.info_screens.ArtistInfoScreen
import com.example.start2.home.screens.info_screens.SongInfoScreen
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.viewmodels.MusicViewModel


@Composable
fun AppNavGraph(
    navController: NavHostController,
    spotifyViewModel: SpotifyViewModel,
    musicViewModel: MusicViewModel
) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.Home.route,
        modifier = Modifier.background(color = Color(61,24,81))
    ) {
        addHomeRoute(navController, musicViewModel, spotifyViewModel)
        addRateRoute(navController,spotifyViewModel)
        addSearchRoute(navController, spotifyViewModel)
        //addFavoritesRoute(navController)
        addRecommendationRoute(navController, spotifyViewModel, musicViewModel)
        addProfileRoute(navController,spotifyViewModel)
        addAnalysisRoute(navController, spotifyViewModel)
    }
}

//home navigation
private fun NavGraphBuilder.addHomeRoute(navController: NavController, musicViewModel: MusicViewModel, spotifyViewModel: SpotifyViewModel) {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        showHome(navController, musicViewModel, spotifyViewModel)
        showHomeDetail(navController)
    }
}
private fun NavGraphBuilder.showHome(navController: NavController, musicViewModel: MusicViewModel, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.Home.route) {
        HomeScreen(
            navController = navController,
            musicViewModel = musicViewModel,
            spotifyViewModel = spotifyViewModel,
            showDetail = {
                navController.navigate(LeafScreen.HomeDetail.route)
            }
        )
    }
}
private fun NavGraphBuilder.showHomeDetail(navController: NavController) {
    composable(route = LeafScreen.HomeDetail.route) {
        HomeDetailScreen(
            onBack = {
                navController.navigateUp()
            }
        )
    }
}
private fun NavGraphBuilder.addRateRoute(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    navigation(
        route = RootScreen.Rate.route,
        startDestination = LeafScreen.Rate.route
    ) {
        showRate(navController, spotifyViewModel)
        showSongInfo(navController, spotifyViewModel)
        showAlbumInfo(navController, spotifyViewModel)
        showPerformerInfo(navController,spotifyViewModel)

    }
}

private fun NavGraphBuilder.showRate(navController: NavController,spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.Rate.route) {
        RateScreen(navController, spotifyViewModel )
    }
}
//end of home navigation

// Recommendation navigation
private fun NavGraphBuilder.addRecommendationRoute(navController: NavController, spotifyViewModel: SpotifyViewModel, musicViewModel: MusicViewModel) {
    navigation(
        route = RootScreen.Recommendation.route,
        startDestination = LeafScreen.Recommendation.route
    )
    {
        showRecommendation(navController, spotifyViewModel, musicViewModel)
        showSongInfo(navController, spotifyViewModel)
        showAlbumInfo(navController, spotifyViewModel)
        showPerformerInfo(navController,spotifyViewModel)
    }
}


private fun NavGraphBuilder.showRecommendation(navController: NavController, spotifyViewModel: SpotifyViewModel, musicViewModel: MusicViewModel) {
    composable(route = LeafScreen.Recommendation.route) {
        RecommendationScreen(navController, spotifyViewModel, musicViewModel )
    }
}

//end of recommendation navigation

//search navigation
private fun NavGraphBuilder.addSearchRoute(navController: NavController,  spotifyViewModel: SpotifyViewModel) {
    navigation(
        route = RootScreen.Search.route,
        startDestination = LeafScreen.Search.route
    ) {
        showSearch(navController, spotifyViewModel)
        showSongInfo(navController, spotifyViewModel)
        showAlbumInfo(navController, spotifyViewModel)
        showPerformerInfo(navController,spotifyViewModel)
    }
}

private fun NavGraphBuilder.showSearch(navController: NavController,  spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.Search.route) {
        SearchScreen(navController, spotifyViewModel)
    }
}

//end of search navigation





//profile navigation
private fun NavGraphBuilder.addProfileRoute(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    navigation(
        route = RootScreen.Profile.route,
        startDestination = LeafScreen.Profile.route
    ) {
        showProfile(navController)
        showFollowers(navController)
        showFriendsScreen(navController,spotifyViewModel)
        showSongInfo(navController, spotifyViewModel)
        showAlbumInfo(navController, spotifyViewModel)
        showPerformerInfo(navController,spotifyViewModel)
    }
}
private fun NavGraphBuilder.showProfile(navController: NavController) {
    composable(route = LeafScreen.Profile.route) {
        ProfileScreen(navController)
    }
}

private fun NavGraphBuilder.showFollowers(navController: NavController) {
    composable(route = LeafScreen.Followers.route) {
        FollowersScreen()
    }
}
//end of profile navigation
//favorites navigation

private fun NavGraphBuilder.addAnalysisRoute(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    navigation(
        route = RootScreen.Analysis.route,
        startDestination = LeafScreen.Analysis.route
    ) {
        showAnalysis(navController, spotifyViewModel)
        showAnalysisChart(navController, spotifyViewModel)
        showSongInfo(navController, spotifyViewModel)
        showAlbumInfo(navController, spotifyViewModel)
        showPerformerInfo(navController,spotifyViewModel)
    }
}
//
private fun NavGraphBuilder.showAnalysis(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.Analysis.route) {
        AnalysisScreen(navController, spotifyViewModel)
    }
}


private fun NavGraphBuilder.showAnalysisChart(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.AnalysisTable.route) {
        AnalysisTableScreen(navController, spotifyViewModel)
    }
}
//end of favorites navigation

//Info Screens

private fun NavGraphBuilder.showSongInfo(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.SongInfo.route) {
        SongInfoScreen(navController,spotifyViewModel)
    }
}
private fun NavGraphBuilder.showPerformerInfo(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.ArtistInfo.route) {
        ArtistInfoScreen(navController,spotifyViewModel)
    }
}
private fun NavGraphBuilder.showAlbumInfo(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.AlbumInfo.route) {
        AlbumInfoScreen(navController,spotifyViewModel)
    }
}

private fun NavGraphBuilder.showFriendsScreen(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.FriendScreen.route) {
        FriendScreen()
    }
}
