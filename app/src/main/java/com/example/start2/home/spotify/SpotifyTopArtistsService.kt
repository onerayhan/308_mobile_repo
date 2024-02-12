package com.example.start2.home.spotify

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyTopArtistsService {
    @GET("v1/me/top/artists")
    suspend fun getUserTopArtists(
        @Header("Authorization") authHeader: String,
        @Query("time_range") range: String = "short_term",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<TopArtistsResponse>
}
object SpotifyTopArtistsServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: SpotifyTopArtistsService by lazy {
        retrofit.create(SpotifyTopArtistsService::class.java)
    }
}