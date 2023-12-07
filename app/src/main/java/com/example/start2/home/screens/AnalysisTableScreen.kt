package com.example.start2.home.screens

import androidx.activity.compose.BackHandler
import kotlin.random.Random
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.home.spotify.Album
import com.example.start2.home.spotify.Artist
import com.example.start2.home.spotify.Image
import com.example.start2.home.spotify.Track
import com.example.start2.home.navigators.LeafScreen
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.start2.home.spotify.SpotifyViewModel


// Enumlar ve data classlar
enum class SortOrder { ASCENDING, DESCENDING, DEFAULT }
enum class SortAttribute { DURATION_MS, NAME, DEFAULT }
data class SortState(val attribute: SortAttribute, var order: SortOrder = SortOrder.DEFAULT)



// Ana ekran
@Composable
fun AnalysisTableScreen(navController: NavController, viewModelspoti: SpotifyViewModel) {
    val topTracks by viewModelspoti.topTracks.observeAsState()
    var sortState by remember { mutableStateOf(SortState(SortAttribute.DEFAULT)) }

    // Başlık metnini burada tanımlayın ve dilediğiniz gibi güncelleyin
    var title by remember { mutableStateOf("Başlık") }

    topTracks?.let { tracks ->
        var sortedTracks by remember { mutableStateOf(getSortedTracks(tracks.items, sortState)) }

        BackHandler {
            navController?.navigateToLeafScreen(LeafScreen.Analysis)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Başlık metnini gösterin
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )

            FilterDropdowns(selectedFilter = "All") { selectedFilter ->
                title = when (selectedFilter) {
                    "All" -> "Tüm Şarkılar"
                    "Recent" -> "En Son Şarkılar"
                    "Popular" -> "Popüler Şarkılar"
                    "Favorites" -> "Favori Şarkılar"
                    else -> "Başlık"
                }
            }


            AnalysisTableHeader(sortState) { attribute ->
                sortState = if (sortState.attribute == attribute && sortState.order != SortOrder.DESCENDING) {
                    sortState.copy(order = SortOrder.DESCENDING)
                } else {
                    SortState(attribute, SortOrder.ASCENDING)
                }
                sortedTracks = getSortedTracks(tracks.items, sortState)
            }
            AnalysisTableContent(sortedTracks)
        }
    } ?: Text("Yükleniyor veya veri yok.")
}

// Tablo başlığı
@Composable
fun AnalysisTableHeader(sortState: SortState, onSortChange: (SortAttribute) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFEEEEEE))
    ) {
        HeaderText("Name", SortAttribute.NAME, sortState, onSortChange)
        Spacer(Modifier.width(16.dp))
        HeaderText("Duration", SortAttribute.DURATION_MS, sortState, onSortChange)
    }
}

// Başlık metni
@Composable
fun HeaderText(text: String, attribute: SortAttribute, sortState: SortState, onSortChange: (SortAttribute) -> Unit) {
    Text(text, Modifier
        .clickable { onSortChange(attribute) }
        .width(100.dp),
        color = if (sortState.attribute == attribute) Color.Blue else Color.Black)
}

// Tablo içeriği
@Composable
fun AnalysisTableContent(songs: List<Track>) {
    LazyColumn {
        items(songs) { song ->
            TrackItem(song)
        }
    }
}

// Şarkı öğesi
@Composable
fun TrackItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Title: ${track.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "Artist: ${track.artists.joinToString { it.name }}",
                color = Color.Black.copy(alpha = 0.7f)
            )
            Text(
                text = "Album: ${track.album.name}",
                color = Color.Black.copy(alpha = 0.7f)
            )
            Text(
                text = "Length: ${track.duration_ms.millisecondsToMinutes()} min",
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

// Yardımcı fonksiyonlar
fun getSortedTracks(tracks: List<Track>, sortState: SortState): List<Track> {
    return when (sortState.attribute) {
        SortAttribute.DURATION_MS -> when (sortState.order) {
            SortOrder.ASCENDING -> tracks.sortedBy { it.duration_ms }
            SortOrder.DESCENDING -> tracks.sortedByDescending { it.duration_ms }
            SortOrder.DEFAULT -> tracks
        }
        SortAttribute.NAME -> when (sortState.order) {
            SortOrder.ASCENDING -> tracks.sortedBy { it.name }
            SortOrder.DESCENDING -> tracks.sortedByDescending { it.name }
            SortOrder.DEFAULT -> tracks
        }

        else -> {tracks}
    }
}

fun Int.millisecondsToMinutes(): String {
    val minutes = this / 60000
    val seconds = (this % 60000) / 1000
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}


@Composable
fun AnalysisTableContent(
    songs: List<Track>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onSongSelect: (String) -> Unit
) {

        Column (modifier = Modifier.fillMaxWidth()){
            // Implement filter dropdowns
            FilterDropdowns(selectedFilter, onFilterChange)

            // Display songs in a LazyColumn
            LazyColumn (){
                items(songs) { song ->
                    TrackItem(song, onSongSelect)
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
fun TrackItem(track: Track, onSongSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onSongSelect(track.id) })
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "${track.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${track.artists.joinToString { it.name }}",
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${track.album.name}",
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${track.duration_ms.millisecondsToMinutes()} min",
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "${track.popularity}",
                color = Color.White.copy(alpha = 0.7f)
            )
            // Add additional attributes here if needed
        }
    }
}



/*
@Composable
<<<<<<< HEAD
fun TrackItem(track: Track, onSongSelect: (String) -> Unit) {
=======
fun SongItem(track: Track, onSongSelect: (String) -> Unit) {
>>>>>>> origin/wrong_analysis
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
}*/

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
