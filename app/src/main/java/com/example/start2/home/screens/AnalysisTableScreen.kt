package com.example.start2.home.screens

import kotlin.random.Random
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.Album
import com.example.start2.Artist
import com.example.start2.Image
import com.example.start2.Track
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.viewmodels.Song
import com.example.start2.viewmodels.SongViewModel
import com.example.start2.viewmodels.SpotifyViewModel


//Stateful
@Composable
fun AnalysisTableScreen(navController: NavController, viewModelspoti: SpotifyViewModel) {
    val viewModel: SongViewModel = viewModel()
    val songs = viewModel.songs
    viewModel.generateDummyVM()
    viewModelspoti.getUserTopTracks()
    val topTracks by viewModelspoti.topTracks.observeAsState()
    val selectedFilter = viewModel.selectedFilter
    Log.d("table", "im open")

    BackHandler {
        navController?.navigateToLeafScreen(LeafScreen.Analysis)
    }
    topTracks?.let { tracks ->
        AnalysisTableContent(
            songs = tracks.items, // Assuming items is a list of Song in your TopTracksResponse
            selectedFilter = "All", // Or your implementation of filter
            onFilterChange = { /* Implement filter logic */ },
            onSongSelect = { songId ->
                Log.d("analysisTable", songId)
                // Handle song selection, e.g., navigate to a detailed view
            }
        )
    } ?: run {
        // Show loading or empty state
    }
}
//Stateless
@Composable
fun AnalysisTableContent(
    songs: List<Track>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onSongSelect: (String) -> Unit
) {
    Column {
        // Implement filter dropdowns
        FilterDropdowns(selectedFilter, onFilterChange)

        // Display songs in a LazyColumn
        LazyColumn {
            items(songs) { song ->
                SongItem(song, onSongSelect)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AnalysisTableContentPreview() {
    AnalysisTableContent(
        songs = generateDummyTracks(),
        selectedFilter = "All",
        onFilterChange = {},
        onSongSelect = {}
    )
}




@Composable
fun SongItem(track: Track, onSongSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onSongSelect(track.id) })
            .fillMaxWidth()
            //.padding(16.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Title: ${track.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Artist: ${track.artists[0].name}",
                //fontStyle = MaterialTheme.typography.body2.fontStyle,
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Album: ${track.album.name}",
                //fontStyle = MaterialTheme.typography.body2.fontStyle,
                color =Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Length: ${track.duration_ms * 1000}",
                //fontStyle = MaterialTheme.typography.body2.fontStyle,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
        // Additional UI elements or styling can be added here
    }
}

@Composable
fun FilterDropdowns(selectedFilter: String, onFilterChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val filters = listOf("All Songs", "Recent", "Popular", "Favorites") // Dummy filter list

    Column {
        Text(
            text = selectedFilter,
            modifier = Modifier
                .clickable(onClick = { expanded = true })
                .padding(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(8.dp)
        ) {
            filters.forEach { filter ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onFilterChange(filter)
                }) {
                    Text(text = filter)
                }
            }
        }
    }
}

fun generateDummyTracks(numTracks: Int = 10): List<Track> {
    val albumNames = listOf("Summer Dreams", "Winter Tales", "Autumn Memories", "Spring Awakenings", "Timeless Notes")
    val artistNames = listOf("Alice Blue", "Ethan Sky", "Nora Fields", "Leo Storm", "Ivy Green")
    val trackNames = listOf("Song of Dawn", "Midnight Melody", "Ocean's Whisper", "Eclipse Echo", "Harmony in Chaos")
    val trackIds = listOf("1", "2", "3", "4", "5")
    val albumIds = listOf("A1", "A2", "A3", "A4", "A5")
    val artistIds = listOf("Ar1", "Ar2", "Ar3", "Ar4", "Ar5")

    return List(numTracks) {
        Track(
            album = Album(
                album_type = "album",
                total_tracks = 10,
                available_markets = listOf("US", "CA"),
                external_urls = mapOf("spotify" to "https://spotify.com"),
                href = "https://api.spotify.com/v1/albums/${albumIds.random()}",
                id = albumIds.random(),
                images = listOf(
                    Image(
                        url = "https://i.scdn.co/image/abcd",
                        height = 640,
                        width = 640
                    )
                ),
                name = albumNames.random(),
                release_date = "2020-01-01",
                release_date_precision = "day",
                type = "album",
                uri = "spotify:album:${albumIds.random()}",
                artists = listOf(
                    Artist(
                        external_urls = mapOf("spotify" to "https://spotify.com"),
                        href = "https://api.spotify.com/v1/artists/${artistIds.random()}",
                        id = artistIds.random(),
                        name = artistNames.random(),
                        type = "artist",
                        uri = "spotify:artist:${artistIds.random()}"
                    )
                )
            ),
            artists = listOf(
                Artist(
                    external_urls = mapOf("spotify" to "https://spotify.com"),
                    href = "https://api.spotify.com/v1/artists/${artistIds.random()}",
                    id = artistIds.random(),
                    name = artistNames.random(),
                    type = "artist",
                    uri = "spotify:artist:${artistIds.random()}"
                )
            ),
            available_markets = listOf("US", "CA"),
            disc_number = 1,
            duration_ms = Random.nextInt(180000, 300000),
            explicit = false,
            external_ids = mapOf("isrc" to "US1234567890"),
            external_urls = mapOf("spotify" to "https://spotify.com/track/${trackIds.random()}"),
            href = "https://api.spotify.com/v1/tracks/${trackIds.random()}",
            id = trackIds.random(),
            name = trackNames.random(),
            popularity = Random.nextInt(0, 100),
            preview_url = "https://p.scdn.co/mp3-preview/abcd",
            track_number = 1,
            type = "track",
            uri = "spotify:track:${trackIds.random()}",
            is_local = false
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