package com.example.start2.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.R
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
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = "Username",
            fontSize = 24.sp
        )
        Text(
            text = "user@email.com",
            fontSize = 18.sp
        )
        Text(
            text = "Followers: 100",
            fontSize = 18.sp,
            modifier = Modifier.clickable {navController?.navigateToLeafScreen(LeafScreen.Followers) })
        Text(
            text = "Following: 50",
            fontSize = 18.sp
        )

        // Favorite Songs List
        FavoriteSongsList()

    }
}


    @Composable
    fun FavoriteSongsList() {
        // TODO:: Berkant -> connect here to the database
        // View Model in activity is needed
        val favoriteSongs = listOf(
            "Song 1",
            "Song 2",
            // ... more songs
        )

        Column {
            favoriteSongs.forEach { song ->
                Text(text = song)
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