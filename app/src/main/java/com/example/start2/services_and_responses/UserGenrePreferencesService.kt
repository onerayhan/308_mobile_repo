package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserGenrePreferencesService {
    @GET("/api/user_genre_preference/{username}")
    suspend fun getUserGenrePreferences(
        @Path("username") username : String
    ): Response<UserGenrePreferencesResponse>

}

object UserGenrePreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: UserGenrePreferencesService by lazy {
        retrofit.create(UserGenrePreferencesService::class.java)
    }
}