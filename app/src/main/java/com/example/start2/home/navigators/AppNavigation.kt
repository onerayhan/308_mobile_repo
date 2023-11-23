package com.example.start2.home.navigators

import FollowersScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.start2.home.ProfileScreen
import com.example.start2.home.screens.AnalysisChartScreen
import com.example.start2.home.screens.AnalysisOption
import com.example.start2.home.screens.AnalysisScreen
import com.example.start2.home.screens.FriendScreen
import com.example.start2.home.screens.HomeDetailScreen
import com.example.start2.home.screens.HomeScreen
import com.example.start2.home.screens.RateScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.Home.route
    ) {
        addHomeRoute(navController)
        addRateRoute(navController)
        //addSearchRoute(navController)
        //addFavoritesRoute(navController)
        addProfileRoute(navController)
        addAnalysisRoute(navController)
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
private fun NavGraphBuilder.addRateRoute(navController: NavController) {
    navigation(
        route = RootScreen.Rate.route,
        startDestination = LeafScreen.Rate.route
    ) {
        showRate(navController)

    }
}

private fun NavGraphBuilder.showRate(navController: NavController) {
    composable(route = LeafScreen.Rate.route) {
        RateScreen()
    }
}
private fun NavGraphBuilder.addProfileRoute(navController: NavController) {
    navigation(
        route = RootScreen.Profile.route,
        startDestination = LeafScreen.Profile.route
    ) {
        showProfile(navController)
        showFollowers(navController)

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

private fun NavGraphBuilder.addAnalysisRoute(navController: NavController) {
    navigation(
        route = RootScreen.Analysis.route,
        startDestination = LeafScreen.Analysis.route
    ) {
        showAnalysis(navController)
        showAnalysisChart(navController)
    }
}
//
private fun NavGraphBuilder.showAnalysis(navController: NavController) {
    composable(route = LeafScreen.Analysis.route) {
        val selectedOption = AnalysisOption.SongPopularity // Provide a default option
        AnalysisScreen(navController, selectedOption)
    }
}


private fun NavGraphBuilder.showAnalysisChart(navController: NavController) {
    composable(route = LeafScreen.AnalysisChart.route) {
        AnalysisChartScreen(navController)
    }
}
//end of favorites navigation
