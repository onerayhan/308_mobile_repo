package com.example.start2.services_and_responses

data class UserFallowingsAlbumPreferencesResponse(
    val albums: List<FallowingsAlbumDetails>
)

data class FallowingsAlbumDetails(
    val album: String,
    val count: Int
)
