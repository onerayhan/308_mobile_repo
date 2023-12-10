package com.example.start2.home.spotify

import SpotifyArtistInfoServiceProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DummySpotifyViewModel(token: String) : SpotifyViewModel(token) {
    private val dummyRepository = DummySpotifyRepository()

    override fun getUserTopTracks() {
        viewModelScope.launch {
            val result = dummyRepository.getUserTopTracks(token, "a")
            result?.let {
                topTracks.postValue(it)
            }
        }
    }
}

class DummySpotifyRepository : SpotifyRepository(SpotifyServiceProvider.instance, SpotifyTopArtistsServiceProvider.instance,SpotifySearchServiceProvider.instance, SpotifyRecommendationsServiceProvider.instance, SpotifyArtistInfoServiceProvider.instance, SpotifyTrackInfoServiceProvider.instance, SpotifyAlbumInfoServiceProvider.instance, SpotifyArtistTopTrackServiceProvider.instance,SpotifyArtistAlbumsServiceProvider.instance, SpotifyAlbumTracksServiceProvider.instance) {
    override suspend fun getUserTopTracks(token: String?, term: String): TopTracksResponse? {
        // Return dummy data
        return TopTracksResponse(
            href = "",
            limit = 0,
            next = null,
            offset = 0,
            previous = null,
            total = 0,
            items = listOf() // Populate with dummy track items if necessary
        )
    }
}
