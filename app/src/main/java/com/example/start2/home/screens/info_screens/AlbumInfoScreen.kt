package com.example.start2.home.screens.info_screens

import DarkColorPalette
import LightColorPalette
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.rememberAsyncImagePainter
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.screens.TrackItem
import com.example.start2.home.screens.millisecondsToMinutes
import com.example.start2.home.spotify.SimpleTrack
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.spotify.Track


@Composable
fun AlbumInfoScreen(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    spotifyViewModel.getSelectedAlbum()
    spotifyViewModel.getAlbumTracks()

    //spotifyViewModel.getSelectedAlbumTracks()
    androidx.compose.material.MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material.MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            //SingerContent()
            val selectedAlbumInfo by spotifyViewModel.selectedAlbumInfo.observeAsState()
            val albumTracks by spotifyViewModel.albumTracks.observeAsState()
            //val albumImageUrl = selectedAlbumInfo?.images?.firstOrNull()?.url
            Log.d("RegistrationActivity", spotifyViewModel._selectedAlbumID.value.toString())
            selectedAlbumInfo?.let { infoResponse ->
                Column {
                    AlbumContent(albumName = infoResponse?.name)
                }


            }
            albumTracks?.let{infoResponse ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    val albumImageUrl = selectedAlbumInfo?.images?.firstOrNull()?.url
                    AlbumTracksContent(
                        songs = infoResponse.items,
                        imageUrl = albumImageUrl,
                        onFilterChange = {},
                        onSongSelect = { songId ->
                            Log.d("analysisParalysis", songId)
                            Log.d("analysisParalysis", "songId")

                            //viewModelScope.launch {
                            spotifyViewModel.saveSelectedTrack(songId)
                            navController.navigateToLeafScreen(LeafScreen.SongInfo)
                        },
                        onArtistSelect = { artistId ->
                            spotifyViewModel.saveSelectedArtist(artistId)
                            navController.navigateToLeafScreen(LeafScreen.ArtistInfo)
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun AlbumContent(
    albumName: String?,
) {
    // Singer's Name with Custom Font
    Text(
        text = "$albumName",
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center)
}

@Composable
fun AlbumTracksContent(
    songs: List<SimpleTrack>,
    onFilterChange: (String) -> Unit,
    imageUrl: String?,
    onSongSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit,
) {
    LazyColumn {
        items(songs) { item ->
            SimpleTrackItem(item, imageUrl, onSongSelect,onArtistSelect)
        }

    }
}


@Composable
fun SimpleTrackItem(
    track: SimpleTrack,
    imageUrl: String?,
    onSongSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //val imageUrl = track.album.images.firstOrNull()?.url ?: "" // Provide a default or error image URL if needed

        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Album Art",
            modifier = Modifier
                .size(100.dp)
                .padding(4.dp)
                .padding(start = 0.dp,top = 0.dp,end = 4.dp, bottom = 0.dp)
                .clip(CircleShape), // Adjust size as needed
            contentScale = ContentScale.Crop // Adjust the scaling as needed
        )
        Column {
            Text(
                text = "${track.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp).clickable( onClick = {onSongSelect(track.id) }),
            )
            Text(
                text = "${track.artists.joinToString { it.name }}",
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp).clickable {
                    val artistId = track.artists.firstOrNull()?.id ?: return@clickable
                    onArtistSelect(artistId)
                }
            )
            Text(
                text = "${track.duration_ms.millisecondsToMinutes()} min",
                color = Color.Black.copy(alpha = 0.7f)
            )
            // Add additional attributes here if needed
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