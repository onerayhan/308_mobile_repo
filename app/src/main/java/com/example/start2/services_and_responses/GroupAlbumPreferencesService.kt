package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST


interface GroupAlbumPreferencesService {
    @POST("/api/group_album_preference")
    suspend fun getGroupAlbumPreferences(
        @Body request: JsonObject
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