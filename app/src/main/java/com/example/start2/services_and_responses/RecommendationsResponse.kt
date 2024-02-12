package com.example.start2.services_and_responses


typealias RecommendationsResponse = List<RecommendationsResponseItem>



data class RecommendationsResponseItem (
    val album: String,
    val genre: String,
    val performer: String,
    val songId: Int,
    val songsName: String,
    val username: String
);