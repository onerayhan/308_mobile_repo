package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserGetSongRatingsService {
    @POST("/api/user_song_ratings")
    suspend fun getUserSongRatings(
        @Body request: JsonObject
    ): Response<UserGetSongRatingsResponse>
}
object UserGetSongRatingsServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.51.167.155/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: UserGetSongRatingsService by lazy {
        retrofit.create(UserGetSongRatingsService::class.java)
    }
}