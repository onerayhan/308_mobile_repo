package com.example.start2.home.spotify

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface  SpotifyArtistTopTrackService {
    @GET("v1/artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Header("Authorization") authHeader: String,
        @Path("id") artistId: String,
        @Query("market") market: String? = "TR"
    ): Response<SpotifyArtistTopTrackResponse>
}


object SpotifyArtistTopTrackServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: SpotifyArtistTopTrackService by lazy {
        retrofit.create(SpotifyArtistTopTrackService::class.java)
    }
}