package com.example.start2.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.start2.SpotifyService
import com.example.start2.SpotifyServiceProvider
import com.example.start2.TopTracksResponse
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class SpotifyViewModel(private val token: String) : ViewModel() {

    private val repository = SpotifyRepository(SpotifyServiceProvider.instance)

    val topTracks = MutableLiveData<TopTracksResponse>()

    fun getUserTopTracks() {
        viewModelScope.launch {
            val result = repository.getUserTopTracks(token)
            result?.let {
                topTracks.postValue(it)
            } // Add error handling if result is null
        }
    }
}

class SpotifyRepository(private val spotifyService: SpotifyService) {
    suspend fun getUserTopTracks(token: String): TopTracksResponse? {
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
}
