package com.example.start2.services_and_responses

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface  AllGenrePreferencesService {
    @GET("/api/get_all_genre_preference")
    suspend fun getAllGenrePreferences(
    ): Response<UserGenrePreferencesResponse>
}

object AllGenrePreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: AllGenrePreferencesService by lazy {
        retrofit.create(AllGenrePreferencesService::class.java)
    }
}