package com.example.start2.services_and_responses

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface AllPerformerPreferencesService {
    @GET("/api/get_all_performer_preference")
    suspend fun getAllPerformerPreferences(
    ): Response<UserPerformerPreferencesResponse>
}

object AllPerformerPreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: AllPerformerPreferencesService by lazy {
        retrofit.create(AllPerformerPreferencesService::class.java)
    }
}