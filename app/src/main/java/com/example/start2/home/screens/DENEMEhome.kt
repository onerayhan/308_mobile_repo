package com.example.start2.home.screens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
            import androidx.compose.ui.unit.dp
            import androidx.documentfile.provider.DocumentFile
            import androidx.compose.foundation.rememberScrollState
            import androidx.compose.foundation.verticalScroll
            import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
            import androidx.navigation.NavController
            import androidx.navigation.compose.rememberNavController
            import com.example.start2.home.Profile.ProfileViewModel
            import com.example.start2.home.Profile.ProfileViewModelFactory
            import com.example.start2.home.Profile.UserPreferences
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.viewmodels.MusicViewModel

@Composable
fun HomeScreen2(
    showDetail: () -> Unit,
    navController: NavController,
    musicViewModel: MusicViewModel
) {
    val batchResult by musicViewModel.batchResult.observeAsState()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val profileViewModel =
        viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val userProfile by profileViewModel.userProfile

    val contentResolver = LocalContext.current.contentResolver

    var songName by remember { mutableStateOf("") }
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

    val inputFields = mutableListOf(
        "Song Name" to songName,
        "Length (HH:MM:SS)" to length,
        "Tempo" to tempo,
        "Album Name" to albumName,
        "Album Release Year" to albumReleaseYear,
        "Performer Name" to performerName,
        "Genre" to genre,
        "Mood" to mood,
        "Instrument" to instrument,
        "Recording Type" to recordingType,
        "Listens" to listens,
        "Release Year (YYYY-MM-DD)" to releaseYear
    )

    var currentIndex by remember { mutableStateOf(0) }
    var tempValue by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF07142E))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Step ${currentIndex + 1} of ${inputFields.size}",
                    color = Color.White, // Metni beyaz renkte ayarlayın
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = tempValue,
                onValueChange = {
                    tempValue = it
                    when (inputFields[currentIndex].first) {
                        "Song Name" -> songName = it
                        "Length (HH:MM:SS)" -> length = it
                        "Tempo" -> tempo = it
                        "Album Name" -> albumName = it
                        "Album Release Year" -> albumReleaseYear = it
                        "Performer Name" -> performerName = it
                        "Genre" -> genre = it
                        "Mood" -> mood = it
                        "Instrument" -> instrument = it
                        "Recording Type" -> recordingType = it
                        "Listens" -> listens = it
                        "Release Year (YYYY-MM-DD)" -> releaseYear = it
                    }
                },
                label = { Text(inputFields[currentIndex].first, color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Song Name: $songName", color = Color.White)
            Text("Length: $length", color = Color.White)
            Text("Tempo: $tempo", color = Color.White)
            Text("Album Name: $albumName", color = Color.White)
            Text("Album Release Year: $albumReleaseYear", color = Color.White)
            Text("Performer Name: $performerName", color = Color.White)
            Text("Genre: $genre", color = Color.White)
            Text("Mood: $mood", color = Color.White)
            Text("Instrument: $instrument", color = Color.White)
            Text("Recording Type: $recordingType", color = Color.White)
            Text("Listens: $listens", color = Color.White)
            Text("Release Year: $releaseYear", color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            selectedFileUri?.let { uri ->
                val file = DocumentFile.fromSingleUri(LocalContext.current, uri)
                val fileName = file?.name ?: "Unknown File"
                Text("Selected File: $fileName", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentIndex > 0) {
                    Button(
                        onClick = {
                            currentIndex--
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Previous", color = Color.White)
                    }
                }

                if (currentIndex < inputFields.size - 1) {
                    Button(
                        onClick = {
                            if (currentIndex < inputFields.size - 1) {

                                currentIndex++
                                tempValue =
                                    inputFields[currentIndex].second // Sonraki alanın değerini yükleyin
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Next", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = {
                            val formatter = SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.getDefault()
                            )
                            val formattedDateTime = formatter.format(Date())
                            val songParams = ProfileViewModel.SongParams(
                                song_name = songName,
                                username = userProfile?.username ?: "aa",
                                length = length,
                                tempo = tempo.toIntOrNull(),
                                recording_type = recordingType,
                                listens = listens.toIntOrNull(),
                                release_year = releaseYear.toIntOrNull(),
                                added_timestamp = formattedDateTime,
                                album_name = albumName,
                                album_release_year = albumReleaseYear.toIntOrNull(),
                                performer_name = performerName,
                                genre = genre,
                                mood = mood,
                                instrument = instrument
                            )

                            // Launch the coroutine to add the song
                            profileViewModel.addSong(songParams)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Next / Add Music", color = Color.White)
                    }
                }
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
                    .padding(top=3.dp)
            ) {
                Text("Submit File", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    val navController = rememberNavController() // Create a NavController
    val musicViewModel = MusicViewModel("aa")
    val spotifyViewModel = SpotifyViewModel("aa")
    HomeScreen(
        showDetail = {},
        navController = navController,
        musicViewModel = musicViewModel,
        spotifyViewModel = spotifyViewModel
    )
}
