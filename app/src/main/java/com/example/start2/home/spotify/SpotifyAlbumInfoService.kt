package com.example.start2.home.spotify

import com.example.start2.home.spotify.Album
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory

interface SpotifyAlbumInfoService {
    @GET("/v1/albums/{id}")
    suspend fun getAlbumInfo(
        @Header("Authorization") authHeader: String,
        @Path("id") albumId: String,
        @Query("market") market: String? = null
    ): Response<Album>
}


object SpotifyAlbumInfoServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifyAlbumInfoService by lazy {
        retrofit.create(SpotifyAlbumInfoService::class.java)
    }
}