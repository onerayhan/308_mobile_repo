package com.example.start2.home.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.spotify.Track
import androidx.lifecycle.viewModelScope
import com.example.start2.home.spotify.SpotifySearchItem
import com.example.start2.viewmodels.MusicViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RecommendationScreen(navController: NavController, viewModelSpoti: SpotifyViewModel ,musicViewModel: MusicViewModel) {
    var sortState by remember { mutableStateOf(SortState(SortAttribute.DEFAULT)) }
    var recommendationQuery: String by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var genresString: String by remember{ mutableStateOf("") }
    var artistsString: String by remember{ mutableStateOf("") }
    var artistId: String by remember{ mutableStateOf("") }

    val options = listOf("getGenrePrefs", "getAlbumPrefs", "getPerformerPrefs")
    val selectedOptions = remember { mutableStateListOf<String>() }
    val userGenrePreferences by musicViewModel.userGenrePreferences.observeAsState()
    val userAlbumPreferences by musicViewModel.userAlbumPreferences.observeAsState()
    val userPerformerPreferences by musicViewModel.userPerformerPreferences.observeAsState()
    val searchResults by viewModelSpoti.searchResults.observeAsState()
    //viewModelSpoti.
    Column {


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptions.joinToString(),
                onValueChange = { },
                label = { Text("Select Preferences") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            if (selectedOptions.contains(option)) {
                                selectedOptions.remove(option)
                            } else {
                                selectedOptions.add(option)
                            }
                        }
                    ) {
                        Text(option)
                        Spacer(Modifier.width(20.dp))
                        if (selectedOptions.contains(option)) {
                            Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                        }
                    }
                }
            }
        }
        Row (modifier = Modifier.fillMaxWidth()){
            Button(onClick = {
                musicViewModel.onOptionSelected(options)
            }) {
                Text("Fetch Preferences")
            }
            Button(onClick = { viewModelSpoti.getRecommendation(genresString, artistId, "") }) {
                Text("Search")
            }
        }


        userGenrePreferences?.let{ response ->
            Log.d("RecomScreen", response.genres.joinToString(separator = ",") { it.genre.lowercase() })

            genresString = response.genres.take(1).joinToString(separator = ",") { it.genre.lowercase() }
        }
        userAlbumPreferences?.let{ response ->
            Log.d("RecomScreen", response.toString())


        }
        userPerformerPreferences?.let{ response ->
            Log.d("RecomScreen",response.toString())

            artistsString = response.performers.random().performer.lowercase()
            //val randomPerformer = response.performers.random().performer.lowercase()

            viewModelSpoti.search(artistsString)
            searchResults?.let{spotiResponse ->
                Log.d("MainHost",spotiResponse.toString())
                artistId= spotiResponse.items.filterIsInstance<SpotifySearchItem.ArtistItem>().firstOrNull()?.artist!!.id

            }

        }
        val recommendationResults by viewModelSpoti.recommendationResults.observeAsState()
        recommendationResults?.let { tracks ->
            Column {
                RecommendationsTableHeader(sortState, onSortChange = { attribute ->
                    if (sortState.attribute == attribute && sortState.order != SortOrder.DESCENDING) {
                        sortState = sortState.copy(order = SortOrder.values()[(sortState.order.ordinal + 1) % SortOrder.values().size])
                    } else {
                        sortState = SortState(attribute, SortOrder.ASCENDING)
                    }
                })
                RecommendationsTableContent(

                    songs = tracks.tracks, // Assuming items is a list of Song in your TopTracksResponse
                    selectedFilter = "All", // Or your implementation of filter
                    onFilterChange = { /* Implement filter logic */ },
                    onSongSelect = { songId  ->
                        Log.d("analysisParalysis", songId)
                        //viewModelScope.launch {
                        viewModelSpoti.saveSelectedTrack(songId)
                        navController.navigateToLeafScreen(LeafScreen.SongInfo)
                    },
                    onAlbumSelect = {albumId->
                        viewModelSpoti.saveSelectedAlbum(albumId)
                        navController.navigateToLeafScreen(LeafScreen.AlbumInfo)
                    },
                    onArtistSelect = {artistId ->
                        viewModelSpoti.saveSelectedArtist(artistId)
                        navController.navigateToLeafScreen(LeafScreen.ArtistInfo)
                    },
                )
            }

        } ?: run{}
    }
}


//stateless

@Composable
fun RecommendationsTableHeader(sortState: SortState, onSortChange: (SortAttribute) -> Unit) {

    Row (modifier = Modifier
        .fillMaxWidth()
        .background(Color.Red)){

        // Create clickable text for each sortable attribute
        androidx.compose.material3.Text("Name", Modifier.clickable { onSortChange(SortAttribute.NAME) })
        Spacer(Modifier.width(8.dp))
        androidx.compose.material3.Text("Duration", Modifier.clickable { onSortChange(SortAttribute.DURATION_MS) })
        // Add more attributes as needed
    }
}


@Composable
fun RecommendationsTableContent(
    songs: List<Track>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onSongSelect: (String) -> Unit,
    onAlbumSelect: (String) -> Unit,
    onArtistSelect: (String) -> Unit,
) {

    Column (modifier = Modifier.fillMaxWidth()){
        // Implement filter dropdowns
        //FilterDropdowns(selectedFilter, onFilterChange)

        // Display songs in a LazyColumn
        LazyColumn (){
            items(songs) { song ->
                TrackItem(song, onSongSelect, onAlbumSelect, onArtistSelect)
            }
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

