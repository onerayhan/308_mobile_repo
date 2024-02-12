package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupGenrePreferencesService {
    @GET("/api/group_genre_preference/{username}")
    suspend fun getGroupGenrePreferences(
        @Path("username") username : String
    ): Response<UserGenrePreferencesResponse>

}

object GroupGenrePreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: GroupGenrePreferencesService by lazy {
        retrofit.create(GroupGenrePreferencesService::class.java)
    }
}