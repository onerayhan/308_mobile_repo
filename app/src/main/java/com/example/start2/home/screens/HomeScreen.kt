package com.example.start2.home.screens



import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope

import androidx.navigation.NavController
import com.example.start2.ProfileViewModel
import com.example.start2.ProfileViewModelFactory
import com.example.start2.UserPreferences
import com.example.start2.viewmodels.MusicViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader


@Composable
fun HomeScreen(
    showDetail: () -> Unit,
    navController: NavController,
    musicViewModel: MusicViewModel
) {

    val batchResult by musicViewModel.batchResult.observeAsState()
    val context = LocalContext.current
    val userPreferences= remember{UserPreferences(context)}
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))



    val contentResolver = LocalContext.current.contentResolver
    var songName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var tempo by remember { mutableStateOf("") }
    var recordingType by remember { mutableStateOf("STUDIO") }
    var listens by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var addedTimestamp by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var albumName by remember { mutableStateOf("") }
    var albumReleaseYear by remember { mutableStateOf("") }
    var performerName by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("") }
    var instrument by remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
        }
    }




    // Set the background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD62964)) // Dark background color
            .padding(16.dp)
    )  {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Song Name and Username
                ExpandableCard(title = "Add Song Manually") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = songName,
                            onValueChange = { songName = it },
                            label = { Text("Song Name", color = Color.White) },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username", color = Color.White) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Other Text Fields...

                    OutlinedTextField(
                        value = length,
                        onValueChange = { length = it },
                        label = { Text("Length (HH:MM:SS)", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    OutlinedTextField(
                        value = tempo,
                        onValueChange = { tempo = it },
                        label = { Text("Tempo", color = Color.White) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = albumName,
                        onValueChange = { albumName = it },
                        label = { Text("Album Name", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = albumReleaseYear,
                        onValueChange = { albumReleaseYear = it },
                        label = { Text("Album Release Year", color = Color.White) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = performerName,
                        onValueChange = { performerName = it },
                        label = { Text("Performer Name", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = genre,
                        onValueChange = { genre = it },
                        label = { Text("Genre", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = mood,
                        onValueChange = { mood = it },
                        label = { Text("Mood", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = instrument,
                        onValueChange = { instrument = it },
                        label = { Text("Instrument", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = recordingType,
                            onValueChange = { recordingType = it },
                            label = { Text("Recording Type", color = Color.White) },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        OutlinedTextField(
                            value = listens,
                            onValueChange = { listens = it },
                            label = { Text("Listens", color = Color.White) },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = releaseYear,
                        onValueChange = { releaseYear = it },
                        label = { Text("Release Year (YYYY-MM-DD)", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = addedTimestamp,
                        onValueChange = { addedTimestamp = it },
                        label = { Text("Added Timestamp (HH:MM:SS)", color = Color.White) },
                        modifier = Modifier.fillMaxWidth()
                    )


                }
                Spacer(modifier = Modifier.height(16.dp))

                // File Upload Section
                Button(
                    onClick = {
                        filePickerLauncher.launch("*/*")
                       },

                         modifier = Modifier
                             .fillMaxWidth()
                             .height(50.dp)
                ) {
                    Text("Select File", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Display selected file name
                selectedFileUri?.let { uri ->
                    val file = DocumentFile.fromSingleUri(LocalContext.current, uri)
                    val fileName = file?.name ?: "Unknown File"
                    Text("Selected File: $fileName", color = Color.White)
                }
                Button(
                    onClick = {
                        selectedFileUri?.let { uri ->
                            Log.d("MusicViewModel", "ALo: $uri")
                            musicViewModel.processFileAndPostTracks(uri, contentResolver)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Submit File", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        val songParams = ProfileViewModel.SongParams(
                            song_name = songName,
                            username = username,
                            length = length,
                            tempo = tempo.toIntOrNull(),
                            recording_type = recordingType,
                            listens = listens.toIntOrNull(),
                            release_year = releaseYear.toIntOrNull(),
                            added_timestamp = addedTimestamp,
                            album_name = albumName,
                            album_release_year = albumReleaseYear.toIntOrNull(),
                            performer_name = performerName,
                            genre = genre,
                            mood = mood,
                            instrument = instrument
                        )

                        // Launch the coroutine to add the song
                        profileViewModel.addSong(songParams)
                            // Enable the button after the operation is complete
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)


                ) {
                    Text("Add Music", color = Color.White)
                }
                ExpandableCard(title = "Export Stuff") {
                    Button(onClick = { /*TODO*/ }) {
                        
                    }
                    Button(onClick = { /*TODO*/ }) {
                        
                    }
                    Button(onClick = { /*TODO*/ }) {
                        
                    }
                    
                }
            }

        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(title: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { expanded = !expanded }
    ) {
        Column {
            Text(
                text = title,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                color = Color.White
            )

            if (expanded) {
                content()
            }
        }
    }
}
