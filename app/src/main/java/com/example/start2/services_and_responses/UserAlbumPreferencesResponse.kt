package com.example.start2.services_and_responses

data class UserAlbumPreferencesResponse(
    val albums: List<AlbumDetails>
)

data class AlbumDetails(
    val album: String,
    val count: Int
)
