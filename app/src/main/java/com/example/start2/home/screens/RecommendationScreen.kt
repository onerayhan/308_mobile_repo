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
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

@Composable
fun RecommendationScreen(navController: NavController, viewModelSpoti: SpotifyViewModel) {
    var sortState by remember { mutableStateOf(SortState(SortAttribute.DEFAULT)) }
    var recommendationQuery: String by remember { mutableStateOf("") }
    //viewModelSpoti.
    Column {
        TextField(
            value = recommendationQuery,
            onValueChange = { recommendationQuery = it },
            label = { Text("Pick one or more Genre(s) for a recommendation(Comma separated)") }
        )
        Button(onClick = { viewModelSpoti.getRecommendation(recommendationQuery) }) {
            Text("Search")
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

    Row (modifier =  Modifier.fillMaxWidth().background(Color.Red)){

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

