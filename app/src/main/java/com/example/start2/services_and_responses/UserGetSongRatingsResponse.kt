package com.example.start2.services_and_responses

data class UserGetSongRatingsResponse (
    val user_song_ratings: List<UserSongRating>
)
data class UserSongRating(
    val song_id: String,
    val rating: Int,
    val rating_timestamp: String // or use a Date type if you want to parse the timestamp
)
