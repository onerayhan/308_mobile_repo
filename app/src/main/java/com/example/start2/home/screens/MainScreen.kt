package com.example.start2.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.start2.R
import com.example.start2.core.AnalysisIcon
import com.example.start2.core.FavoriteIcon
import com.example.start2.core.HomeIcon
import com.example.start2.core.ProfileIcon
import com.example.start2.core.RateIcon
import com.example.start2.core.RecommendationIcon
import com.example.start2.core.SearchIcon
import com.example.start2.home.navigators.AppNavGraph
import com.example.start2.home.navigators.RootScreen
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.viewmodels.MusicViewModel


@Composable
fun MainScreen(viewModel: SpotifyViewModel, musicViewModel: MusicViewModel) {
    val navController = rememberNavController()
    val currentSelectedScreen by navController.currentScreenAsState()
    val currentRoute by navController.currentRouteAsState()
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentSelectedScreen = currentSelectedScreen)
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AppNavGraph(navController = navController, spotifyViewModel = viewModel, musicViewModel = musicViewModel)
        }
    }
}
//@Preview()
@Composable
private fun BottomNavBar(
    navController: NavController,
    currentSelectedScreen: RootScreen
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Home,
            onClick = { navController.navigateToRootScreen(RootScreen.Home) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.home))
            },
            icon = {
                HomeIcon()
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Search,
            onClick = { navController.navigateToRootScreen(RootScreen.Search) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.search))
            },
            icon = {
                SearchIcon()
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Recommendation,
            onClick = { navController.navigateToRootScreen(RootScreen.Recommendation) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.recommendation))
            },
            icon = {
                RecommendationIcon()
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Profile,
            onClick = { navController.navigateToRootScreen(RootScreen.Profile) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.profile))
            },
            icon = {
                ProfileIcon()
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Rate,
            onClick = { navController.navigateToRootScreen(RootScreen.Rate) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.rate))
            },
            icon = {
                RateIcon()
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == RootScreen.Analysis,
            onClick = { navController.navigateToRootScreen(RootScreen.Analysis) },
            alwaysShowLabel = true,
            label = {
                Text(text = stringResource(id = R.string.analysis))
            },
            icon = {
                AnalysisIcon()
            }
        )
    }
}
@Preview
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Home) }
    DisposableEffect(key1 = this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == RootScreen.Home.route } -> {
                    selectedItem.value = RootScreen.Home
                }
                destination.hierarchy.any { it.route == RootScreen.Search.route } -> {
                    selectedItem.value = RootScreen.Search
                }
                destination.hierarchy.any { it.route == RootScreen.Recommendation.route } -> {
                    selectedItem.value = RootScreen.Recommendation
                }
                destination.hierarchy.any { it.route == RootScreen.Profile.route } -> {
                    selectedItem.value = RootScreen.Profile
                }
                destination.hierarchy.any { it.route == RootScreen.Rate.route } -> {
                    selectedItem.value = RootScreen.Rate
                }
                destination.hierarchy.any { it.route == RootScreen.Analysis.route } -> {
                    selectedItem.value = RootScreen.Analysis
                }

            }

        }
        addOnDestinationChangedListener(listener)
        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}
@Preview
@Stable
@Composable
private fun NavController.currentRouteAsState(): State<String?> {
    val selectedItem = remember { mutableStateOf<String?>(null) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.route
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }
    return selectedItem
}

private fun NavController.navigateToRootScreen(rootScreen: RootScreen) {
    navigate(rootScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}