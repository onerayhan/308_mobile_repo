package com.example.start2.services_and_responses

data class UserFollowingsAlbumPreferencesResponse(
    val albums: List<FollowingsAlbumDetails>
)

data class FollowingsAlbumDetails(
    val album: String,
    val count: Int
)
