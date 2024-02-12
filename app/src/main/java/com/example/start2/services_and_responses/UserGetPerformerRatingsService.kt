package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserGetPerformerRatingsService {
    @POST("/api/user_performer_ratings")
    suspend fun getUserPerformerRatings(
        @Body request: JsonObject
    ): Response<UserGetPerformerRatingsResponse>
}

object UserGetPerformerRatingsServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: UserGetPerformerRatingsService by lazy {
        retrofit.create(UserGetPerformerRatingsService::class.java)
    }
}