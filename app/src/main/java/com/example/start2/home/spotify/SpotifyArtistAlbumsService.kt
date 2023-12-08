package com.example.start2.home.spotify

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface  SpotifyArtistAlbumsService {
    @GET("v1/artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Header("Authorization") authHeader: String,
        @Path("id") artistId: String,
        @Query("include_groups") includeGroups: String = "single,appears_on",
        @Query("market") market: String? = "TR",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<SpotifyArtistAlbumsResponse>
}


object SpotifyArtistAlbumsServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: SpotifyArtistAlbumsService by lazy {
        retrofit.create(SpotifyArtistAlbumsService::class.java)
    }
}