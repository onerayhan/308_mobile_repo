package com.example.start2.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel


// ViewModel handling state
class SongViewModel : ViewModel() {
    val songs = mutableStateListOf<Song>()
    var selectedFilter by mutableStateOf("")
    //songs = generateDummySongs()
    fun changeFilter(newFilter: String) {
        selectedFilter = newFilter
        // Implement filtering logic
    }
    fun generateDummyVM() {
        songs.addAll(generateDummySongs())
    }
}

data class Song(
    val id: String,
    val title: String,
    val singer: String,
    val album : String,
    val length : String,    // other properties...


)


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



