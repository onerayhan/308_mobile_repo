package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface UserPerformerPreferencesService {
    @GET("/api/user_performer_preference/{username}")
    suspend fun getUserPerformerPreferences(
        @Path("username") username : String
    ): Response<UserPerformerPreferencesResponse>
}

object UserPerformerPreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: UserPerformerPreferencesService by lazy {
        retrofit.create(UserPerformerPreferencesService::class.java)
    }
}