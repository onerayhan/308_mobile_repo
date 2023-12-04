package com.example.start2.home.screens.info_screens

import DarkColorPalette
import LightColorPalette
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.start2.home.spotify.SpotifyViewModel


@Composable
fun SongInfoScreen(navController: NavController,spotifyViewModel: SpotifyViewModel) {
    spotifyViewModel.getSelectedTrack()
    androidx.compose.material.MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material.MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            //SingerContent()
            val selectedTrackInfo by spotifyViewModel.selectedTrackInfo.observeAsState()
            Log.d("RegistrationActivity", spotifyViewModel._selectedTrackID.value.toString())
            selectedTrackInfo.let { infoResponse ->
                Log.d("RegistrationActivity", infoResponse?.href.toString())
                Column {

                    TrackContent(trackName = infoResponse?.name)
                }

            }

        }
    }
}

@Composable
fun TrackContent(
    trackName: String?,
) {
    // Singer's Name with Custom Font
    Text(text = "a $trackName", fontWeight = FontWeight.Bold, fontSize = 30.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}