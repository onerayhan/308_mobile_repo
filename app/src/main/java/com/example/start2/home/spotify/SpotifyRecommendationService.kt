package com.example.start2.home.spotify


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyRecommendationsService {
    @GET("/v1/recommendations")
    suspend fun getRecommendations(
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int? = null,
        @Query("market") market: String? = null,
        @Query("seed_artists") seedArtists: String? = null,
        @Query("seed_genres") seedGenres: String? = null,
        @Query("seed_tracks") seedTracks: String? = null
        // Add more query parameters as needed
    ): Response<SpotifyRecommendationsResponse>
}

object SpotifyRecommendationsServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifyRecommendationsService by lazy {
        retrofit.create(SpotifyRecommendationsService::class.java)
    }
}


