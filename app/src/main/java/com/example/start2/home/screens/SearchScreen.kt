package com.example.start2.home.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.start2.home.spotify.Album
import com.example.start2.home.spotify.Artist
import com.example.start2.home.spotify.Track
import com.example.start2.home.spotify.SpotifySearchItem
import com.example.start2.home.spotify.SpotifyViewModel

@Composable
fun SearchScreen(navController: NavController, viewModel: SpotifyViewModel) {
    var searchQuery: String by remember { mutableStateOf("") }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") }
        )
        Button(onClick = { viewModel.search(searchQuery) }) {
            Text("Search")
        }

        val searchResults by viewModel.searchResults.observeAsState()
        searchResults?.let {
            SearchResultContent(
                items = it.items,
                onSongSelect = {songId ->},
                onAlbumSelect = {albumId ->},
                onArtistSelect = {artistId ->}

            )
        } ?: run{}
    }
}

@Composable
fun SearchResultContent(
    items: List<SpotifySearchItem>,
    onSongSelect: (String) -> Unit,
    onAlbumSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            when (item) {
                is SpotifySearchItem.TrackItem -> TrackItem(track = item.track, onSongSelect, onAlbumSelect , onArtistSelect )
                is SpotifySearchItem.AlbumItem -> AlbumItem(album = item.album)
                is SpotifySearchItem.ArtistItem -> ArtistItem(artist = item.artist)
            }
        }
    }
}



@Composable
fun AlbumItem(album: Album) {
    // Layout for album item
}

@Composable
fun ArtistItem(artist: Artist) {
    // Layout for artist item
}

