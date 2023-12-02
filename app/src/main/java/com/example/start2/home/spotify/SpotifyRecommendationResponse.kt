package com.example.start2.home.spotify

data class SpotifyRecommendationsResponse(
    val seeds: List<Seed>,
    val tracks: List<Track>
)

data class Seed(
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int,
    val href: String?,
    val id: String,
    val initialPoolSize: Int,
    val type: String
)

// Existing classes: Track, Album, Artist, Image
