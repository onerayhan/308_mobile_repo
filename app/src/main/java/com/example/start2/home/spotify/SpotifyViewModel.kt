package com.example.start2.home.spotify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.math.log


open class SpotifyViewModel(protected val token: String) : ViewModel() {

    private val repository = SpotifyRepository(SpotifyServiceProvider.instance, SpotifySearchServiceProvider.instance)

    val topTracks = MutableLiveData<TopTracksResponse>()

    val searchResults = MutableLiveData<SpotifySearchResponse>()


    open fun getUserTopTracks() {
        viewModelScope.launch {
            val result = repository.getUserTopTracks(token)
            result?.let {
                topTracks.postValue(it)
            } // Add error handling if result is null
        }
    }
    
    open fun search(searchQuery: String) {
        viewModelScope.launch { 
            val result = repository.search(token, searchQuery)
            result?.let {
                searchResults.postValue(it)
            }
        }
    }
}

open class SpotifyRepository(private val spotifyService: SpotifyService, private val spotifySearchService: SpotifySearchService) {
    open suspend fun getUserTopTracks(token: String?): TopTracksResponse? {
        return try {
            val response = spotifyService.getUserTopTracks("Bearer $token")
            if (response.isSuccessful) {
                response.body()
            } else {
                null // or handle error response
            }
        } catch (e: Exception) {
            null // or handle exception
        }
    }
    
    open suspend fun search(token: String?, searchQuery : String) : SpotifySearchResponse? {
        return try {
            Log.d("RegistrationActivity", "Every time I see your face")
            val response = spotifySearchService.searchSpotify("Bearer $token", searchQuery, "track,album,artist")
            if (response.isSuccessful) {
                Log.d("RegistrationActivity", "I know the love that lies and waits for me")
                parseSpotifySearchResponse(response.body())
                //response.body()
            } else {
                Log.d("RegistrationActivity", "And every time I look at you")
                null
            }
        } catch(e: Exception) {
            null
        }
        
    }

    private fun parseSpotifySearchResponse(rawResponse: RawSpotifySearchResponse?): SpotifySearchResponse {
        val items = mutableListOf<SpotifySearchItem>()

        rawResponse?.tracks?.items?.forEach { track ->
            items.add(SpotifySearchItem.TrackItem(track))
        }

        rawResponse?.albums?.items?.forEach { album ->
            items.add(SpotifySearchItem.AlbumItem(album))
        }

        rawResponse?.artists?.items?.forEach { artist ->
            items.add(SpotifySearchItem.ArtistItem(artist))
        }
        //TODO:: assign proper href values
        return SpotifySearchResponse(

            href = "", // Assuming these fields are at the top level of the response
            limit = 0,
            next = "",
            offset = 0,
            previous = "",
            total = 0,
            items = items,
        )
    }
}





