package com.example.start2.services_and_responses

data class UserPerformerPreferencesResponse (
    val performers: List<PerformerDetails>
)

data class PerformerDetails(
    val performer: String,
    val count: Int
)