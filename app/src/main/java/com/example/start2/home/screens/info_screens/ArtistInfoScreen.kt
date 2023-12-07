package com.example.start2.home.screens.info_screens

import DarkColorPalette
import LightColorPalette
import SingerContent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.screens.TrackItem
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.spotify.TopTracksResponse
import com.example.start2.home.spotify.Track


@Composable
fun ArtistInfoScreen(navController: NavController,spotifyViewModel: SpotifyViewModel) {
    spotifyViewModel.getSelectedArtist()
    Log.d("RegistrationActivity", "Tadimiz kacmasin ali riza bey")
    spotifyViewModel.getSelectedArtistTopTracks()
    androidx.compose.material.MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material.MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            //SingerContent()
            val selectedArtistInfo by spotifyViewModel.selectedArtistInfo.observeAsState()
            val selectedArtistTopTracks by spotifyViewModel.selectedArtistTopTracks.observeAsState()
            Log.d("RegistrationActivity", spotifyViewModel._selectedArtistID.value.toString())
            selectedArtistInfo?.let { infoResponse ->
                Log.d("RegistrationActivity", infoResponse?.href.toString())
                Column {

                    ArtistContent(singerName = infoResponse?.name)

                    //ArtistsAlbumContent(albums = infoResponse?.)
                }

            } ?: run{}
            selectedArtistTopTracks?.let {topTracksResponse ->
                Column(modifier = Modifier.fillMaxWidth()){
                    Log.d("RegistrationActivity", topTracksResponse.tracks.toString())
                    ArtistInfoContent(
                        songs = topTracksResponse.tracks,
                        onFilterChange = {},
                        onSongSelect = {songId  ->
                            Log.d("analysisParalysis", songId)
                            //viewModelScope.launch {
                            spotifyViewModel.saveSelectedTrack(songId)
                            navController.navigateToLeafScreen(LeafScreen.SongInfo)},
                        onAlbumSelect = {albumId->
                            spotifyViewModel.saveSelectedAlbum(albumId)
                            navController.navigateToLeafScreen(LeafScreen.AlbumInfo)},
                        onArtistSelect = {artistId ->
                            spotifyViewModel.saveSelectedArtist(artistId)
                            navController.navigateToLeafScreen(LeafScreen.ArtistInfo)}
                    )
                }
            } ?: run{}
        }
    }
}

@Composable
fun ArtistContent(
    singerName : String?,
) {
    Text(text = "a $singerName", fontWeight = FontWeight.Bold, fontSize = 30.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}
@Composable
fun ArtistInfoContent(
    songs: List<Track>,
    onFilterChange: (String) -> Unit,
    onSongSelect: (String) -> Unit,
    onAlbumSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit,
) {
    LazyColumn {
        items(songs) { item ->
            TrackItem(
                track = item,
                onSongSelect =  {},
                onAlbumSelect = {},
                onArtistSelect ={}
            )
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