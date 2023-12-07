package com.example.start2.home.navigators

import FollowersScreen
import SingerScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.start2.home.ProfileScreen

import com.example.start2.home.screens.AnalysisOption

import com.example.start2.home.screens.AnalysisTableScreen
import com.example.start2.home.screens.AnalysisScreen
import com.example.start2.home.screens.HomeDetailScreen
import com.example.start2.home.screens.HomeScreen
import com.example.start2.home.screens.RateScreen
import com.example.start2.home.screens.RecommendationScreen
import com.example.start2.home.screens.SearchScreen
import com.example.start2.home.screens.info_screens.AlbumInfoScreen
import com.example.start2.home.screens.info_screens.PerformerInfoScreen
import com.example.start2.home.screens.info_screens.SongInfoScreen
import com.example.start2.home.spotify.SpotifyViewModel


@Composable
fun AppNavGraph(
    navController: NavHostController,
    spotifyViewModel: SpotifyViewModel
) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.Home.route
    ) {
        addHomeRoute(navController)
        addRateRoute(navController,spotifyViewModel)
        addSearchRoute(navController, spotifyViewModel)
        //addFavoritesRoute(navController)
        addRecommendationRoute(navController, spotifyViewModel)
        addProfileRoute(navController,spotifyViewModel)
        addAnalysisRoute(navController, spotifyViewModel)
    }
}

//home navigation
private fun NavGraphBuilder.addHomeRoute(navController: NavController) {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.route
    ) {
        showHome(navController)
        showHomeDetail(navController)
    }
}
private fun NavGraphBuilder.showHome(navController: NavController) {
    composable(route = LeafScreen.Home.route) {
        HomeScreen(
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
        showRate(navController)
        showSongInfo(navController, spotifyViewModel)
        showAlbumInfo(navController, spotifyViewModel)
        showPerformerInfo(navController,spotifyViewModel)

    }
}

private fun NavGraphBuilder.showRate(navController: NavController) {
    composable(route = LeafScreen.Rate.route) {
        RateScreen()
    }
}
//end of home navigation

// Recommendation navigation
private fun NavGraphBuilder.addRecommendationRoute(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    navigation(
        route = RootScreen.Recommendation.route,
        startDestination = LeafScreen.Recommendation.route
    )
    {
        showRecommendation(navController, spotifyViewModel)
        showSongInfo(navController, spotifyViewModel)
        showAlbumInfo(navController, spotifyViewModel)
        showPerformerInfo(navController,spotifyViewModel)
    }
}


private fun NavGraphBuilder.showRecommendation(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.Recommendation.route) {
        RecommendationScreen(navController, spotifyViewModel )
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
        val selectedOption = AnalysisOption.SongPopularity // Provide a default option
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
        SingerScreen(navController,spotifyViewModel)
    }
}
private fun NavGraphBuilder.showAlbumInfo(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    composable(route = LeafScreen.AlbumInfo.route) {
        AlbumInfoScreen(navController,spotifyViewModel)
    }
}
