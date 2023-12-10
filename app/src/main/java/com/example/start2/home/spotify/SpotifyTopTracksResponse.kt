package com.example.start2.home.spotify
data class TopTracksResponse(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Track>
)

data class Track(
    val album: Album,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: Map<String, String>,
    val external_urls: Map<String, String>,
    val href: String,
    val id: String,
    val name: String,
    val popularity: Int,
    val preview_url: String?,
    val track_number: Int,
    val type: String,
    val uri: String,
    val is_local: Boolean
)

data class Album(
    val album_type: String,
    val total_tracks: Int,
    val available_markets: List<String>,
    val external_urls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val type: String,
    val uri: String,
    val artists: List<Artist>
)

data class Artist(
    val external_urls: Map<String, String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val type: String,
    val uri: String
)

data class Image(
    val url: String,
    val height: Int,
    val width: Int
)
