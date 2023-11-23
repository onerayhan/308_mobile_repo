package com.example.start2.home.screens

import kotlin.random.Random
import android.util.Log
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
import com.example.start2.viewmodels.Song
import com.example.start2.viewmodels.SongViewModel


//Stateful
@Composable
fun AnalysisTableScreen(navController: NavController) {
    val viewModel: SongViewModel = viewModel()
    val songs = viewModel.songs
    viewModel.generateDummyVM()
    val selectedFilter = viewModel.selectedFilter
    Log.d("table", "im open")

    AnalysisTableContent(
        songs = songs,
        selectedFilter = selectedFilter,
        onFilterChange = viewModel::changeFilter,
        onSongSelect = { songId -> Log.d("analysisTable", songId)
            // Handle song selection, e.g., navigate to a detailed view
        }
    )
}
//Stateless
@Composable
fun AnalysisTableContent(
    songs: List<Song>,
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
        songs = generateDummySongs(),
        selectedFilter = "All",
        onFilterChange = {},
        onSongSelect = {}
    )
}




@Composable
fun SongItem(song: Song, onSongSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onSongSelect(song.id) })
            .fillMaxWidth()
            //.padding(16.dp)
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Title: ${song.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Artist: ${song.singer}",
                //fontStyle = MaterialTheme.typography.body2.fontStyle,
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Album: ${song.album}",
                //fontStyle = MaterialTheme.typography.body2.fontStyle,
                color =Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Length: ${song.length}",
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

fun generateDummySongs(numSongs: Int = 10): List<Song> {
    val titles = listOf("Song of Dawn", "Midnight Melody", "Ocean's Whisper", "Eclipse Echo", "Harmony in Chaos")
    val singers = listOf("Alice Blue", "Ethan Sky", "Nora Fields", "Leo Storm", "Ivy Green")
    val albums = listOf("Summer Dreams", "Winter Tales", "Autumn Memories", "Spring Awakenings", "Timeless Notes")
    val lengths = listOf("3:45", "4:20", "2:55", "5:00", "3:30")
    val ids = listOf("1","2","3","4","5")

    return List(numSongs) {
        Song(
            title = titles.random(),
            singer = singers.random(),
            album = albums.random(),
            length = lengths.random(),
            id =  ids.random()
            )
    }
}

