package com.example.start2.services_and_responses

data class UserGetAlbumRatingsResponse(
    val user_album_ratings: List<UserAlbumRating>
)

data class UserAlbumRating(
    val album_id: String,
    val rating: Int,
    val rating_timestamp: String // or use a Date type if you want to parse the timestamp
)
