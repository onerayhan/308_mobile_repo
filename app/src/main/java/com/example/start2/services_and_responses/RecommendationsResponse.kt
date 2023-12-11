package com.example.start2.services_and_responses

data class RecommendationsResponse (
    val album: String,
    val genre: String,
    val performer: String,
    val songId: Int,
    val songsName: String,
    val username: String
);