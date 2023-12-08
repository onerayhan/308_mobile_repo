package com.example.start2.home.screens.info_screens

import DarkColorPalette
import LightColorPalette
import SingerContent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.rememberAsyncImagePainter
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.screens.TrackItem
import com.example.start2.home.spotify.SpotifyArtistInfoResponse
import com.example.start2.home.spotify.SpotifySearchItem
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.spotify.TopTracksResponse
import com.example.start2.home.spotify.Track
import com.example.start2.swipecomponents.Item


@Composable
fun ArtistInfoScreen(navController: NavController,spotifyViewModel: SpotifyViewModel) {
    spotifyViewModel.getSelectedArtist()
    spotifyViewModel.getSelectedArtistTopTracks()
    spotifyViewModel.getSelectedArtistAlbums()
    Log.d("RegistrationActivity", "Tadimiz kacmasin ali riza bey")
    val selectedArtistInfo by spotifyViewModel.selectedArtistInfo.observeAsState()
    val selectedArtistTopTracks by spotifyViewModel.selectedArtistTopTracks.observeAsState()
    val selectedArtistAlbums by spotifyViewModel.selectedArtistAlbums.observeAsState()

    androidx.compose.material.MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material.MaterialTheme.colors.background)
                .padding(16.dp)

        ) {
            //SingerContent()
            Log.d("RegistrationActivity", spotifyViewModel._selectedArtistID.value.toString())
            selectedArtistInfo?.let { infoResponse ->
                Log.d("RegistrationActivity", infoResponse?.href.toString())
                Column {

                    ArtistContent(singer = infoResponse)

                    //ArtistsAlbumContent(albums = infoResponse?.)
                }

            } ?: run{}
            selectedArtistAlbums?.let{artistAlbumsResponse ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Log.d("RegistrationActivity", artistAlbumsResponse.href)
                    ArtistAlbumContent(
                        albums = artistAlbumsResponse.items,
                        onFilterChange = {},
                        onAlbumSelect = {albumId ->
                            spotifyViewModel.saveSelectedAlbum(albumId)
                            navController.navigateToLeafScreen(LeafScreen.AlbumInfo)},
                        onArtistSelect = {}

                    )

                }
            } ?: run{}

            selectedArtistTopTracks?.let {topTracksResponse ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Log.d("RegistrationActivity", topTracksResponse.tracks.toString())
                    ArtistInfoContent(
                        songs = topTracksResponse.tracks,
                        onFilterChange = {},
                        onSongSelect = { songId ->
                            Log.d("analysisParalysis", songId)
                            Log.d("analysisParalysis", "songId")

                            //viewModelScope.launch {
                            spotifyViewModel.saveSelectedTrack(songId)
                            navController.navigateToLeafScreen(LeafScreen.SongInfo)
                        },
                        onAlbumSelect = { albumId ->
                            spotifyViewModel.saveSelectedAlbum(albumId)
                            navController.navigateToLeafScreen(LeafScreen.AlbumInfo)
                        },
                        onArtistSelect = { artistId ->
                            spotifyViewModel.saveSelectedArtist(artistId)
                            navController.navigateToLeafScreen(LeafScreen.ArtistInfo)
                        }

                    )
                }

            } ?: run{}


        }
    }
}

@Composable
fun ArtistContent(
    singer : SpotifyArtistInfoResponse,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,) {
        val imageUrl = singer?.images?.firstOrNull()?.url ?: "" // Provide a default or error image URL if needed
        Text(
            text = "${singer?.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
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
            TrackItem(item, onSongSelect,onAlbumSelect,onArtistSelect)
        }

        val imageUrl = songs.last().album.images.firstOrNull()?.url ?: ""
        item {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Singer Photo",
                modifier = Modifier
                    .size(140.dp)
                    .fillMaxWidth()
                    .padding(4.dp)
                    .padding(start = 0.dp, top = 1.dp, end = 0.dp, bottom = 0.dp)
                    , // Adjust size as needed
                contentScale = ContentScale.FillWidth // Adjust the scaling as needed
            )
        }
    }
}


@Composable
fun ArtistAlbumContent(
    albums: List<com.example.start2.home.spotify.Album>,
    onFilterChange: (String) -> Unit,
    onAlbumSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit,
) {
    LazyRow() {
        items(albums) { item ->
            SpotifyAlbumItem(item,onAlbumSelect,onArtistSelect)
        }

    }
}

@Composable
fun SpotifyAlbumItem(
    album: com.example.start2.home.spotify.Album,
    onAlbumSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            ,
        verticalArrangement = Arrangement.SpaceBetween,

    ) {
        //Item(album.images[0].url)
        val imageUrl = album.images.firstOrNull()?.url ?: ""
        androidx.compose.foundation.Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Album Art",
            modifier = Modifier
                .size(65.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .clickable { onAlbumSelect(album.id) }, // Adjust size as needed
            contentScale = ContentScale.Crop // Adjust the scaling as needed
        )

        androidx.compose.material.Text(
            text = if(album.name.length < 17) album.name else "${album.name.substring(0,14)}...",
            color = androidx.compose.material.MaterialTheme.colors.onBackground,
            modifier = Modifier.clickable(onClick = { onAlbumSelect(album.id) })
        )
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