package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface UserPerformerPreferencesService {
    @POST("/api/user_performer_preference")
    suspend fun getUserPerformerPreferences(
        @Body request: JsonObject
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