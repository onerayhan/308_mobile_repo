package com.example.start2.services_and_responses

data class UserFollowingsPerformerPreferencesResponse(
    val performers: List<FollowingsPerformerDetails>
)

data class FollowingsPerformerDetails(
    val performer: String,
    val count: Int
)