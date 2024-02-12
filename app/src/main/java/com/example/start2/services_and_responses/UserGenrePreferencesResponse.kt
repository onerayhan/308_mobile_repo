package com.example.start2.services_and_responses

data class UserGenrePreferencesResponse(
    val genres: List<GenreDetails>
)

data class GenreDetails(
    val genre: String,
    val count: Int
)
