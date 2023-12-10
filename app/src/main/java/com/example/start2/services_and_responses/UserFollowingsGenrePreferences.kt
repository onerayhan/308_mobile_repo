package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserFollowingsGenrePreferencesService {
    @POST("/api/user_followings_genre_preference")
    suspend fun getFollowingsUserGenrePreferences(
        @Body request: JsonObject
    ): Response<UserGenrePreferencesResponse>
}

object UserFollowingsGenrePreferencesServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: UserFollowingsGenrePreferencesService by lazy {
        retrofit.create(UserFollowingsGenrePreferencesService::class.java)
    }
}