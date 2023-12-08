package com.example.start2.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.start2.viewmodels.MusicViewModel


@Composable
fun MusicAddPage(musicViewModel: MusicViewModel ) {
    var songName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var tempo by remember { mutableStateOf("") }
    var recordingType by remember { mutableStateOf("") }
    var listens by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var addedTimestamp by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<android.net.Uri?>(null) }

    // Set the background color
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF950EAC)) // Dark background color
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Song Name and Username
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
                label = { Text("Release Year", color = Color.White) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = addedTimestamp,
                onValueChange = { addedTimestamp = it },
                label = { Text("Added Timestamp (YYYY-MM-DD HH:MM:SS)", color = Color.White) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // File Upload Section
            Button(
                onClick = {
                    // TODO: Handle file selection logic here
                    // For example, you can use Intent to open a file picker
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

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    // Form submit action
                    // TODO: Handle the form submission logic here
                    // For example, you can create a Music object with the entered data
                    // and perform any necessary operations like saving to a database.
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Add Music", color = Color.White)
            }
        }
    }
}
