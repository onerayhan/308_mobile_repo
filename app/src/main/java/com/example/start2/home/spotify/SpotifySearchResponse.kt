package com.example.start2.home.spotify

sealed class SpotifySearchItem {
    data class TrackItem(val track: Track) : SpotifySearchItem()
    data class AlbumItem(val album: Album) : SpotifySearchItem()
    data class ArtistItem(val artist: Artist) : SpotifySearchItem()
}


data class RawSpotifySearchResponse(
    val tracks: TracksResponse,
    val albums: AlbumsResponse,
    val artists: ArtistsResponse
    // Add any additional fields if the Spotify API includes them in the response.
    // For instance, if there are fields at the top level of the JSON response that
    // are not specific to albums, tracks, or artists, they should be declared here.
)

// The AlbumsResponse, TracksResponse, and ArtistsResponse classes are as previously defined.

data class SpotifySearchResponse(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<SpotifySearchItem>,
)

data class AlbumsResponse(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Album>
)

data class TracksResponse(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Track>
)

data class ArtistsResponse(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<Artist>
)
