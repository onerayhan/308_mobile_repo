package com.example.start2.home.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.spotify.Track
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.example.start2.home.spotify.SpotifySearchItem
import com.example.start2.services_and_responses.RecommendationsResponseItem
import com.example.start2.viewmodels.MusicViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RecommendationScreen2(navController: NavController, viewModelSpoti: SpotifyViewModel ,musicViewModel: MusicViewModel) {
    var sortState by remember { mutableStateOf(SortState(SortAttribute.DEFAULT)) }
    var recommendationQuery: String by remember { mutableStateOf("") }
    var genresString: String by remember{ mutableStateOf("") }
    var artistsString: String by remember{ mutableStateOf("") }
    var artistId: String by remember{ mutableStateOf("") }


    var expanded by remember { mutableStateOf(false) }
    var expandedFirst by remember { mutableStateOf(false) }
    val firstOptions = listOf("MyPrefs", "FollowingsPrefs", "GroupPrefs", "AllPrefs")
    val options = listOf("getGenrePrefs", "getAlbumPrefs", "getPerformerPrefs")
    var selectedFirstOption by remember { mutableStateOf("") }
    val selectedOptions = remember { mutableStateListOf<String>() }

    val userGenrePreferences by musicViewModel.userGenrePreferences.observeAsState()
    val userAlbumPreferences by musicViewModel.userAlbumPreferences.observeAsState()
    val userPerformerPreferences by musicViewModel.userPerformerPreferences.observeAsState()
    val searchResults by viewModelSpoti.searchResults.observeAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Spotify Preferences", "Music Preferences")

    //viewModelSpoti.
    Column {

        ExposedDropdownMenuBox(
            expanded = expandedFirst,
            onExpandedChange = { expandedFirst = !expandedFirst }
        ) {
            TextField(
                readOnly = true,
                value = selectedFirstOption,
                onValueChange = { },
                label = { Text("Select Preference Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFirst) }
            )
            ExposedDropdownMenu(
                expanded = expandedFirst,
                onDismissRequest = { expandedFirst = false }
            ) {
                firstOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedFirstOption = option
                            expandedFirst = false
                        }
                    ) {
                        Text(option)
                    }
                }
            }
        }
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
                val tempOptions = selectedOptions.map { opt ->
                    "${selectedFirstOption.replace("Prefs", "")}${opt.replace("get", "")}"
                }
                musicViewModel.onOptionSelected(tempOptions)
            }) {
                Text("Fetch Preferences")
            }
            Button(onClick = {
                when(selectedTabIndex) {
                    0 -> {
                        viewModelSpoti.getRecommendation(genresString, artistId, "")
                    }
                    1 -> {
                        musicViewModel.getRecommendation(selectedOptions.joinToString(separator = ","),selectedFirstOption.toString() )
                    }
                }
            }) {
                Text("Search")
            }
        }
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }


        userGenrePreferences?.let{ response ->
            Log.d("RecomScreen", response.genres.maxByOrNull { it.count }?.genre?.lowercase() ?: "")
            genresString = response.genres.maxByOrNull { it.count }?.genre?.lowercase() ?: ""

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



        // Display content based on the selected tab
        when (selectedTabIndex) {
            0 -> {
                val recommendationResults by viewModelSpoti.recommendationResults.observeAsState()
                recommendationResults?.let { tracks ->
                    Column {
                        RecommendationsTableHeader2(sortState, onSortChange = { attribute ->
                            if (sortState.attribute == attribute && sortState.order != SortOrder.DESCENDING) {
                                sortState = sortState.copy(order = SortOrder.values()[(sortState.order.ordinal + 1) % SortOrder.values().size])
                            } else {
                                sortState = SortState(attribute, SortOrder.ASCENDING)
                            }
                        })
                        RecommendationsTableContent2(

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
            1 -> {
                val recommendationResults by musicViewModel.recommendationResults.observeAsState()
                recommendationResults?.let { tracks ->
                    Column {
                        RecommendationsTableDbHeader2(sortState, onSortChange = { attribute ->
                            if (sortState.attribute == attribute && sortState.order != SortOrder.DESCENDING) {
                                sortState = sortState.copy(order = SortOrder.values()[(sortState.order.ordinal + 1) % SortOrder.values().size])
                            } else {
                                sortState = SortState(attribute, SortOrder.ASCENDING)
                            }
                        })
                        RecommendationsTableDbContent2(

                            songs = tracks, // Assuming items is a list of Song in your TopTracksResponse
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

                        )
                    }

                } ?: run{}

            }
        }

    }
}


//stateless

@Composable
fun RecommendationsTableHeader2(sortState: SortState, onSortChange: (SortAttribute) -> Unit) {

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
fun RecommendationsTableDbHeader2(sortState: SortState, onSortChange: (SortAttribute) -> Unit) {

    Row (modifier = Modifier
        .fillMaxWidth()
        .background(Color.Red)){

        // Create clickable text for each sortable attribute
        androidx.compose.material3.Text("Name", Modifier.clickable { onSortChange(SortAttribute.NAME) })
        Spacer(Modifier.width(8.dp))
    }
}
@Composable
fun RecommendationsTableContent2(
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

@Composable
fun RecommendationsTableDbContent2(
    songs: List<RecommendationsResponseItem>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onSongSelect: (String) -> Unit,
    onAlbumSelect: (String) -> Unit,
) {

    Column (modifier = Modifier.fillMaxWidth()){
        // Implement filter dropdowns
        //FilterDropdowns(selectedFilter, onFilterChange)

        // Display songs in a LazyColumn
        LazyColumn (){
            items(songs) { song ->
                RecomItem(song, onSongSelect, onAlbumSelect)
            }
        }
    }

}


@Composable
fun RecomItem(
    track: RecommendationsResponseItem,
    onSongSelect: (String) -> Unit,
    onAlbumSelect: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(31,44,71), RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            androidx.compose.material3.Text(
                text = "${track.songsName}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .clickable(onClick = { onSongSelect(track.songId.toString()) }),
            )
            androidx.compose.material3.Text(
                text = "${track.performer}",
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(bottom = 4.dp)

            )
            androidx.compose.material3.Text(
                text = "${track.album}",
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .clickable { onAlbumSelect(track.album) }
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

