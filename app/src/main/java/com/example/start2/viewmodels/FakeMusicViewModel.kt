package com.example.start2.viewmodels

import com.example.start2.services_and_responses.UserAlbumPreferencesResponse
import com.example.start2.services_and_responses.UserGenrePreferencesResponse

class FakeMusicViewModel(username: String) : MusicViewModel(username) {

    // Mock data for testing
    private val fakeMusics = listOf(
        Music("Fake Song", "3:30", 120, "Studio", 1000, "2021", "2021-01-01", "Fake Album", "Fake Artist", "Pop", "Happy", "Guitar")
    )


    // and provide fake responses.

    override fun postTracks(tracks: List<Music>) {
        // Assume the operation is always successful in the fake implementation
        batchResult.postValue(true)
    }

    override fun parseAndSaveMusics(fileContent: String) {
        // Directly post the fake music data
        parsedMusics.postValue(fakeMusics)
    }

    // Additional functions can be overridden depending on what aspects of the ViewModel you are testing
}
