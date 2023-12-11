package com.example.start2.home.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.start2.ProfileViewModel
import com.example.start2.ProfileViewModelFactory
import com.example.start2.R
import com.example.start2.UserPreferences
import com.example.start2.home.spotify.SpotifySearchItem
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.swipecomponents.Item
import kotlinx.coroutines.delay


enum class DisplayContent {
    RateSuggestions,
    SearchResults
}



@Composable
fun RateScreen(navController: NavController, viewModelSpoti: SpotifyViewModel) {
    var sortState by remember { mutableStateOf(SortState(SortAttribute.DEFAULT)) }
    var rateQuery: String by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val profileViewModel =
        viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    var contentToShow by remember { mutableStateOf(DisplayContent.RateSuggestions) }


    Column {
        TextField(
            value = rateQuery,
            onValueChange = { rateQuery = it },
            label = { Text("Enter criteria for rate suggestions") }
        )
        Button(onClick = {
            contentToShow = DisplayContent.RateSuggestions
            viewModelSpoti.getRecommendation(rateQuery, "", "")
        }) {
            Text("Get Suggestions")
        }
        Button(onClick = {
            contentToShow = DisplayContent.SearchResults
            viewModelSpoti.search(rateQuery)
        }) {
            Text("Search Suggestions")
        }

        val rateSuggestions by viewModelSpoti.recommendationResults.observeAsState()
        val searchResults by viewModelSpoti.searchResults.observeAsState()
        when (contentToShow) {
            DisplayContent.RateSuggestions -> {
                rateSuggestions?.let { tracks ->
                    RateContent(
                        songs = tracks.tracks, // Assuming items is a list of Track
                        onSongSelect = { songId ->
                            // Handle song selection, if needed
                        }
                    ) { trackId, rating ->
                        rateSuggestions!!.tracks.find { it.id == trackId }?.let { track ->
                            // Handle rating change, if needed
                            profileViewModel.addSongtr(track.name, track.id, rating)


                        }
                        //viewModelSpoti.rateTrack(trackId, rating)

                        viewModelSpoti.removeRatedTrack(trackId)
                        // Implement this in your ViewModel
                    }
                }
            }

            DisplayContent.SearchResults -> {
                searchResults?.let { tracks ->
                    RateContentSearch(
                        // ... existing code for displaying search results ...
                        songs = tracks.items.filterIsInstance<SpotifySearchItem.TrackItem>(), // Assuming items is a list of Track
                        onSongSelect = { songId ->}
                    )
                    {
                        trackId, rating ->
                        searchResults!!.items.filterIsInstance<SpotifySearchItem.TrackItem>().find { it.track.id == trackId }?.let { track ->
                            // Handle rating change, if needed
                            profileViewModel.addSongtr(track.track.name, track.track.id, rating)


                        }
                        //viewModelSpoti.rateTrack(trackId, rating)

                        viewModelSpoti.removeRatedTrackSearch(trackId)
                    }
                }
            }

        }
    }
}

    @Composable
    fun RateContentSearch(
        songs: List<SpotifySearchItem.TrackItem>,
        onSongSelect: (String) -> Unit,
        onRatingChanged: (String, Int) -> Unit
    ) {
        LazyColumn {
            items(songs, key = { it.track.id }) { song ->
                TrackRateItem(
                    track = song.track,
                    onSongSelect = onSongSelect,
                    onAlbumSelect = {},
                    onArtistSelect = {},
                    onRatingChanged = onRatingChanged,
                )
            }
        }
    }

    @Composable
    fun RateContent(
        songs: List<com.example.start2.home.spotify.Track>,
        onSongSelect: (String) -> Unit,
        onRatingChanged: (String, Int) -> Unit
    ) {
        LazyColumn {
            items(songs, key = { it.id }) { song ->
                TrackRateItem(
                    track = song,
                    onSongSelect = onSongSelect,
                    onAlbumSelect = {},
                    onArtistSelect = {},
                    onRatingChanged = onRatingChanged,
                )
            }
        }
    }



    val accounts = mutableListOf<Item>(
        Item(
            "https://images.unsplash.com/photo-1668069574922-bca50880fd70?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "Musician",
            "Alice (25)"
        ),
        Item(
            "https://images.unsplash.com/photo-1618641986557-1ecd230959aa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "Developer",
            "Chris (33)"
        ),
        Item(
            "https://images.unsplash.com/photo-1667935764607-73fca1a86555?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=688&q=80",
            "Teacher",
            "Roze (22)"
        )
    )


    @Composable
    fun RateScreenPreview() {
        //RateScreen()
    }

    @Composable
    fun StarRating(rating: Int, onRatingChanged: (Int) -> Unit) {
        Row {
            (1..5).forEach { index ->
                Icon(
                    painter = painterResource(
                        id = if (index <= rating) R.drawable.star_full else R.drawable.star_empty
                    ),
                    contentDescription = "Rating Star",
                    modifier = Modifier
                        .clickable { onRatingChanged(index) }
                        .padding(4.dp),
                    tint = if (index <= rating) Color.Yellow else Color.Gray
                )
            }
        }
    }

    @Composable
    fun TrackRateItem(
        track: com.example.start2.home.spotify.Track,
        onSongSelect: (String) -> Unit,
        onAlbumSelect: (String) -> Unit,
        onArtistSelect: (String) -> Unit,
        onRatingChanged: (String, Int) -> Unit // Additional callback for rating change
    ) {
        var rating by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSongSelect(track.id) }
                .padding(8.dp)
        ) {
            Text(text = track.name, color = Color.White,fontWeight = FontWeight.Bold, fontSize = 26.sp)
            Text(text = track.artists.first().name,color = Color.White)
            // Other track details like album and artist
            StarRating(rating = rating) { newRating ->
                rating = newRating
                onRatingChanged(track.id, newRating)
            }
        }
    }
