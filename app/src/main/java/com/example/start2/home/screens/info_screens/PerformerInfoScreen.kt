package com.example.start2.home.screens.info_screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.start2.home.spotify.SpotifyViewModel


@Composable
fun PerformerInfoScreen(navController: NavController,spotifyViewModel: SpotifyViewModel) {

    Scaffold {
            innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding) ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "AlbumInfo Screen", style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}