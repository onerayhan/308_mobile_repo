package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserGetAlbumRatingsService {
    @POST("/api/user_album_ratings")
    suspend fun getUserAlbumRatings(
        @Body request: JsonObject
    ): Response<UserGetAlbumRatingsResponse>
}

object UserGetAlbumRatingsServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.51.167.155/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: UserGetAlbumRatingsService by lazy {
        retrofit.create(UserGetAlbumRatingsService::class.java)
    }
}