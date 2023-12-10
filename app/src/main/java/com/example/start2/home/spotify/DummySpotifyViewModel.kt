package com.example.start2.home.spotify

import SpotifyArtistInfoServiceProvider
import androidx.lifecycle.viewModelScope
import com.example.start2.services_and_responses.AddMobileTokenServiceProvider
import kotlinx.coroutines.launch

class DummySpotifyViewModel(token: String) : SpotifyViewModel(token) {
    private val dummyRepository = DummySpotifyRepository()

    override fun getUserTopTracks() {
        viewModelScope.launch {
            val result = dummyRepository.getUserTopTracks(token, "a", 0)
            result?.let {
                topTracks.postValue(it)
            }
        }
    }
}

class DummySpotifyRepository : SpotifyRepository(SpotifyServiceProvider.instance, SpotifyTopArtistsServiceProvider.instance,SpotifySearchServiceProvider.instance, SpotifyRecommendationsServiceProvider.instance, SpotifyArtistInfoServiceProvider.instance, SpotifyTrackInfoServiceProvider.instance, SpotifyAlbumInfoServiceProvider.instance, SpotifyArtistTopTrackServiceProvider.instance,SpotifyArtistAlbumsServiceProvider.instance, SpotifyAlbumTracksServiceProvider.instance, SpotifyTokenDataServiceProvider.instance, AddMobileTokenServiceProvider.instance) {
    override suspend fun getUserTopTracks(token: String?, term: String , offset: Int): TopTracksResponse? {
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
