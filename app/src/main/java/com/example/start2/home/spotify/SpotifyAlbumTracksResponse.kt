package com.example.start2.home.spotify

data class SpotifyAlbumTracksResponse (
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<SimpleTrack>
)

data class SimpleTrack(
    val artists: List<Artist>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_urls: Map<String, String>,
    val href: String,
    val id: String,
    val name: String,
    val preview_url: String?,
    val track_number: Int,
    val type: String,
    val uri: String,
    val is_local: Boolean
)

