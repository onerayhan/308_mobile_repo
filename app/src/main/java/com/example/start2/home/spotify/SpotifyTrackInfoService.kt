package com.example.start2.home.spotify

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

// SpotifyTrackInfoService Interface
interface SpotifyTrackInfoService {
    @GET("/v1/tracks/{id}")
    suspend fun getTrackInfo(
        @Header("Authorization") authHeader: String,
        @Path("id") trackId: String,
        @Query("market") market: String? = null
    ): Response<Track>
}

// Retrofit service provider for SpotifyTrackInfoService
object SpotifyTrackInfoServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifyTrackInfoService by lazy {
        retrofit.create(SpotifyTrackInfoService::class.java)
    }
}
