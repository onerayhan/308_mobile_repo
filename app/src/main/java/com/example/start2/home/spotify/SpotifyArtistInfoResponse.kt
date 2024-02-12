package com.example.start2.home.spotify


data class SpotifyArtistInfoResponse(
    val external_urls: Map<String, String>,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<ImageObject>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)

data class Followers(
    val href: String?, // Always null as per current API documentation
    val total: Int
)

data class ImageObject(
    val url: String,
    val height: Int?, // Nullable as per API documentation
    val width: Int? // Nullable as per API documentation
)
