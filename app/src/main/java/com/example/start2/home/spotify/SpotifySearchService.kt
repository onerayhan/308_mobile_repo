package com.example.start2.home.spotify

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifySearchService {
    @GET("/v1/search")
    suspend fun searchSpotify(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("type") type: String
    ): Response<RawSpotifySearchResponse> // Adjust based on your response model
}

object SpotifySearchServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifySearchService by lazy {
        retrofit.create(SpotifySearchService::class.java)
    }
}