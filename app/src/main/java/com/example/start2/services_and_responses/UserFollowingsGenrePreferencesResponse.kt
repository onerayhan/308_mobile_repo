package com.example.start2.services_and_responses

data class UserFollowingsGenrePreferencesResponse(
    val genres: List<FollowingsGenreDetails>
)

data class FollowingsGenreDetails(
    val genre: String,
    val count: Int
)
