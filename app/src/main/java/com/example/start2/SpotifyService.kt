package com.example.start2

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyService {
    @GET("v1/me/top/tracks")
    suspend fun getUserTopTracks(@Header("Authorization") authHeader: String): Response<TopTracksResponse>
}
object SpotifyServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: SpotifyService by lazy {
        retrofit.create(SpotifyService::class.java)
    }
}
