package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAlbumPreferencesService {
    @GET("/api/user_album_preference/{username}")
    suspend fun getUserAlbumPreferences(
        @Path("username") username : String
    ): Response<UserAlbumPreferencesResponse>
}

object UserAlbumPreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: UserAlbumPreferencesService by lazy {
        retrofit.create(UserAlbumPreferencesService::class.java)
    }
}