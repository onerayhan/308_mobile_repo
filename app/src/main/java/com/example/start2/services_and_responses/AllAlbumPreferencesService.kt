package com.example.start2.services_and_responses

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface AllAlbumPreferencesService{
    @GET("api/get_all_album_preference")
    suspend fun getAllAlbumPreferences(
    ): Response<UserAlbumPreferencesResponse>
}

object AllAlbumPreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: AllAlbumPreferencesService by lazy {
        retrofit.create(AllAlbumPreferencesService::class.java)
    }
}