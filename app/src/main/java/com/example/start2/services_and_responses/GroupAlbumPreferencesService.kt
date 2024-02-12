package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface GroupAlbumPreferencesService {
    @GET("/api/group_album_preference/{username}")
    suspend fun getGroupAlbumPreferences(
        @Path("username") username : String
    ):  Response<UserAlbumPreferencesResponse>
}

object GroupAlbumPreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: GroupAlbumPreferencesService by lazy {
        retrofit.create(GroupAlbumPreferencesService::class.java)
    }
}