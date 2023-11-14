package com.example.start2.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.navigators.RootScreen

@Composable
fun ProfileScreen(navController: NavController?) {
    val username = "John Doe"
    val email = "john.doe@example.com"
    val followersCount = 100
    val followingCount = 50
    val favoriteSongs = listOf(
        "Danza Kuduro", "Toca Toca", "Black Day", "Nectarines", "Your Mind"
    )

    Column {
        Text("Username: $username")
        Text("Email: $email")
        Text("Followers: $followersCount")
        Text("Following: $followingCount", Modifier.clickable {
            navController?.navigateToLeafScreen(LeafScreen.Followers)
        })

        LazyColumn {
            items(favoriteSongs) { song ->
                Text(song)
            }
        }
    }
}

private fun NavController.navigateToLeafScreen(leafScreen: LeafScreen) {
    navigate(leafScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}