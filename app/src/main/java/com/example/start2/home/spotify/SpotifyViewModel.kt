package com.example.start2.home.spotify

import SpotifyArtistInfoService
import SpotifyArtistInfoServiceProvider
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


open class SpotifyViewModel(protected val token: String) : ViewModel() {

    private val repository = SpotifyRepository(SpotifyServiceProvider.instance,SpotifyTopArtistsServiceProvider.instance, SpotifySearchServiceProvider.instance,
        SpotifyRecommendationsServiceProvider.instance, SpotifyArtistInfoServiceProvider.instance, SpotifyTrackInfoServiceProvider.instance,
        SpotifyAlbumInfoServiceProvider.instance, SpotifyArtistTopTrackServiceProvider.instance, SpotifyArtistAlbumsServiceProvider.instance,
        SpotifyAlbumTracksServiceProvider.instance)


    val selectedTerm = MutableLiveData<String>("short_term")
    val topTracks = MutableLiveData<TopTracksResponse>()

    val topArtists = MutableLiveData<TopArtistsResponse>()

    val searchResults = MutableLiveData<SpotifySearchResponse>()

    val recommendationResults = MutableLiveData<SpotifyRecommendationsResponse>()


    val selectedArtistInfo = MutableLiveData<SpotifyArtistInfoResponse>()
    val selectedTrackInfo = MutableLiveData<Track>()
    val selectedAlbumInfo = MutableLiveData<Album>()
    val selectedArtistTopTracks = MutableLiveData<SpotifyArtistTopTrackResponse>()
    val selectedArtistAlbums = MutableLiveData<SpotifyArtistAlbumsResponse>()

    val albumTracks = MutableLiveData<SpotifyAlbumTracksResponse>()

    val _selectedArtistID = MutableLiveData<String>()
    val _selectedTrackID = MutableLiveData<String>()
    val _selectedAlbumID = MutableLiveData<String>()



    // Public immutable data which the UI can observe


    fun saveSelectedTerm(term: String) {
        val result = term
        result?.let {
            selectedTerm.postValue(it)
        }
    }

    fun saveSelectedArtist(artistID: String) {
        val result = artistID
        result?.let {
            _selectedArtistID.postValue(it)
        }
    }
    fun saveSelectedTrack(trackID: String) {
        val result = trackID
        result?.let {
            _selectedTrackID.postValue(it)
        }
    }

    fun saveSelectedAlbum(albumID: String) {
        val result = albumID
        result?.let {
            _selectedAlbumID.postValue(it)
        }
    }
    open fun getUserTopTracksBatch() {
        viewModelScope.launch {
            var offset = 0
            val accumulatedTracks = mutableListOf<Track>()
            var lastResponse: TopTracksResponse? = null

            repeat(6) {
                val result = repository.getUserTopTracks(token, selectedTerm.value.toString(), offset)
                lastResponse = result
                result?.items?.let {
                    accumulatedTracks.addAll(it)
                } // Handle null results appropriately
                offset += 50
            }

            lastResponse?.let {
                topTracks.postValue(TopTracksResponse(
                    href = it.href,
                    limit = it.limit,
                    next = it.next,
                    offset = offset,
                    previous = it.previous,
                    total = offset,
                    items = accumulatedTracks
                ))
            }
        }
    }
    open fun getUserTopArtistsBatch() {
        viewModelScope.launch {
            var offset = 0
            val accumulatedArtists = mutableListOf<Artist>()
            var lastResponse: TopArtistsResponse? = null

            repeat(6) {
                val result = repository.getUserTopArtist(token, selectedTerm.value.toString(), offset)
                lastResponse = result
                result?.items?.let {
                    Log.d("Mainhost", "${it.joinToString(separator = ",") }")
                    accumulatedArtists.addAll(it)
                } // Handle null results appropriately
                offset += 50
            }

            lastResponse?.let {
                topArtists.postValue(TopArtistsResponse(
                    href = it.href,
                    limit = it.limit,
                    next = it.next,
                    offset = offset,
                    previous = it.previous,
                    total = offset,
                    items = accumulatedArtists
                ))
            }
        }
    }

    open fun getUserTopTracks() {
        viewModelScope.launch {
            val result = repository.getUserTopTracks(token, selectedTerm.value.toString(), 0)
            result?.let {
                topTracks.postValue(it)
            } // Add error handling if result is null
        }
    }
    open fun getUserTopArtists() {
        viewModelScope.launch {
            val result = repository.getUserTopArtist(token, selectedTerm.value.toString(), 0)
            result?.let {
                topArtists.postValue(it)
            } // Add error handling if result is null
        }
    }

    open fun getSelectedArtist() {
        viewModelScope.launch {
            val result = repository.getSelectedArtistInfo(token, _selectedArtistID.value.toString())
            result?.let{
                selectedArtistInfo.postValue(it)
            }
        }
    }
    open fun getSelectedArtistTopTracks() {
        viewModelScope.launch {
            val result = repository.getSelectedArtistTopTracks(token, _selectedArtistID.value.toString())
            result?.let {
                selectedArtistTopTracks.postValue(it)
            }
        }
    }

    open fun getSelectedArtistAlbums() {
        viewModelScope.launch {
            val result = repository.getSelectedArtistAlbums(token, _selectedArtistID.value.toString())
            result?.let {
                selectedArtistAlbums.postValue(it)
            }
        }
    }
    open fun getSelectedTrack() {
        viewModelScope.launch {
            val result = repository.getSelectedTrackInfo(token, _selectedTrackID.value.toString())
            result?.let {
                selectedTrackInfo.postValue(it)
            }
        }
    }

    open fun getSelectedAlbum() {
        viewModelScope.launch{
            val result = repository.getSelectedAlbumInfo(token, _selectedAlbumID.value.toString())
            result?.let {
                selectedAlbumInfo.postValue(it)
            }
        }
    }
    open fun getAlbumTracks() {
        viewModelScope.launch {
            val result = repository.getAlbumTracks(token, _selectedAlbumID.value.toString())
            result?.let {
                albumTracks.postValue(it)
            }
        }
    }

    open fun removeRatedTrack(trackId: String) {
        val currentTracks = recommendationResults.value?.tracks?.toMutableList()
        currentTracks?.let {
            it.removeAll { track -> track.id == trackId }
            recommendationResults.postValue(recommendationResults.value?.copy(tracks = it))
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

    open fun getRecommendation(recommendationQuery: String) {
        viewModelScope.launch {
            val result = repository.getRecommendation(token,recommendationQuery,"","")
            result?.let {
                recommendationResults.postValue(it)
            }
        }
    }

    open fun rateTrack(trackId: String, rating: Int) {
        viewModelScope.launch{
            val result = repository.rateTrack(trackId, rating)
            result?.let {
                //TODO::
            }
        }

    }
}

open class SpotifyRepository(private val spotifyTopTracksService: SpotifyTopTracksService, private val spotifyTopArtistsService: SpotifyTopArtistsService, private val spotifySearchService: SpotifySearchService,
                             private val spotifyRecommendationsService: SpotifyRecommendationsService, private val spotifyArtistInfoService: SpotifyArtistInfoService,
                             private val spotifyTrackInfoService: SpotifyTrackInfoService, private val spotifyAlbumInfoService : SpotifyAlbumInfoService,
                             private val spotifyArtistTopTrackService: SpotifyArtistTopTrackService, private val spotifyArtistAlbumsService: SpotifyArtistAlbumsService,
                             private val spotifyAlbumTracksService: SpotifyAlbumTracksService) {
    open suspend fun getUserTopTracks(token: String?, term: String , offset: Int): TopTracksResponse? {
        return try {
            val response = spotifyTopTracksService.getUserTopTracks("Bearer $token" , range = term, limit = 50 , offset = offset)
            if (response.isSuccessful) {
                response.body()
            } else {
                null // or handle error response
            }
        } catch (e: Exception) {
            null // or handle exception
        }
    }

    open suspend fun getUserTopArtist(token: String?, term: String, offset: Int) : TopArtistsResponse? {
        return try {
            val response = spotifyTopArtistsService.getUserTopArtists(authHeader = "Bearer $token", range = term, limit = 50, offset = offset)
            if(response.isSuccessful) {
                response.body()
            } else{
                null
            }

        } catch (e: Exception) {
            null
        }
    }
    open suspend fun getSelectedArtistInfo(token: String?, selectedArtistId : String) : SpotifyArtistInfoResponse? {
        return try {
            Log.d("RegistrationActivity", "Pickle Riiiiick")
            val response = spotifyArtistInfoService.getArtistInfo("Bearer $token", selectedArtistId)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                Log.d("RegistrationActivity", "And every time I look at you")
                null
            }
        } catch(e: Exception) {
            null
        }
    }
    open suspend fun getSelectedArtistTopTracks(token: String?, selectedArtistId: String) : SpotifyArtistTopTrackResponse? {
        return try {
            Log.d("RegistrationActivity", "Hello There buradayım")
            val response = spotifyArtistTopTrackService.getArtistTopTracks("Bearer $token", selectedArtistId)
            Log.d("RegistrationActivity", response.code().toString())
            if(response.isSuccessful) {
                Log.d("RegistrationActivity", "Hello There satoptracks")
                Log.d("RegistrationActivity", response.body().toString())
                Log.d("RegistrationActivity", "Hello There satoptracks")
                response.body()

            }
            else {
                Log.d("Registration Activity", "Hello There")
                null
            }

        }
        catch (e: Exception) {

            Log.d("RegistrationActivity", "ao")
            null
        }
    }
    open suspend fun getSelectedArtistAlbums(token: String?,selectedArtistId: String): SpotifyArtistAlbumsResponse? {
        return try {
            val response = spotifyArtistAlbumsService.getArtistAlbums("Bearer $token",selectedArtistId )
            if(response.isSuccessful){
                response.body()
            }
            else {
                null
            }
        }
        catch (e: Exception) {
            null
        }
    }
    open suspend fun getAlbumTracks(token: String?, selectedAlbumId: String): SpotifyAlbumTracksResponse?{
        return try{
            val response = spotifyAlbumTracksService.getAlbumTracks("Bearer $token", selectedAlbumId)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }

        } catch(e:Exception){
            null
        }
    }
    open suspend fun getSelectedTrackInfo(token: String?, selectedTrackId: String) : Track?{
        return try {
            //Log.d()
            val response = spotifyTrackInfoService.getTrackInfo("Bearer $token", selectedTrackId)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }
        }
        catch(e: Exception) {
            null
        }
    }

    open suspend fun getSelectedAlbumInfo(token: String?, selectedAlbumId: String) : Album? {
        return try {
            val response = spotifyAlbumInfoService.getAlbumInfo("Bearer $token", selectedAlbumId)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }
        }
        catch(e: Exception) {
            null
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
//TODO:: Implement user entry recommendation
    open suspend fun getRecommendation(token: String?, recommendationQuery: String , artistsQuery: String, trackQuery: String) : SpotifyRecommendationsResponse? {
        return try {
            Log.d("RegistrationActivity", "big in japan")
            val response = spotifyRecommendationsService.getRecommendations("Bearer $token", 10, "TR", seedGenres = recommendationQuery, seedArtists = artistsQuery, seedTracks = trackQuery)
            if(response.isSuccessful) {
                Log.d("RegistrationActivity", response.body().toString())
                response.body()
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

    open suspend fun rateTrack(trackId: String, rating: Int): RateResponse? {
        return null
    }
}





