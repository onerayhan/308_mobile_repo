package com.example.start2.services_and_responses

data class UserGetPerformerRatingsResponse(
    val user_performer_ratings: List<UserPerformerRating>
)

data class UserPerformerRating(
    val performer_id: String,
    val rating: Int,
    val rating_timestamp: String // or use a Date type if you want to parse the timestamp
)
