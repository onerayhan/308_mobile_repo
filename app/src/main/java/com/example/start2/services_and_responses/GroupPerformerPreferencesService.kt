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

interface GroupPerformerPreferencesService {
    @GET("/api/group_performer_preference/{username}")
    suspend fun getGroupPerformerPreferences(
        @Path("username") username : String
    ): Response<UserPerformerPreferencesResponse>
}

object GroupPerformerPreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: GroupPerformerPreferencesService by lazy {
        retrofit.create(GroupPerformerPreferencesService::class.java)
    }
}