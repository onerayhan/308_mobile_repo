package com.example.start2.home.spotify

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface SpotifyGetSeveralTracksService {
    @GET("v1/tracks")
    suspend fun getSeveralTracks(
        @Header("Authorization") authHeader: String,
        @Query("market") market: String? = null,
        @Query("ids") ids: String? = null
    ): Response<SpotifyGetSeveralTracksResponse>
}
object SpotifyGetSeveralTracksServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifyGetSeveralTracksService by lazy {
        retrofit.create(SpotifyGetSeveralTracksService::class.java)
    }
}
