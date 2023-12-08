package com.example.start2.home.spotify

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyAlbumTracksService {
    @GET("v1/albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Header("Authorization") authHeader: String,
        @Path("id") albumId: String,
        @Query("market") market: String? = "TR",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<SpotifyAlbumTracksResponse>
}

object SpotifyAlbumTracksServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: SpotifyAlbumTracksService by lazy {
        retrofit.create(SpotifyAlbumTracksService::class.java)
    }
}