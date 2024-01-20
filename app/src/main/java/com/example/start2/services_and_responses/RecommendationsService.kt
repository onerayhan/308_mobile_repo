package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendationsService {
    @POST("/api/recommendations")
    suspend fun getRecommendation(
        @Body request: JsonObject
    ): Response<RecommendationsResponse>
}

object RecommendationsServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: RecommendationsService by lazy {
        retrofit.create(RecommendationsService::class.java)
    }
}