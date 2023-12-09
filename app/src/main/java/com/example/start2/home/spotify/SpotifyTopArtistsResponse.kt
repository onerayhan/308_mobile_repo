package com.example.start2.home.spotify

data class TopArtistsResponse(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Artist>
)
