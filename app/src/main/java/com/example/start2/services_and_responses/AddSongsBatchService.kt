package com.example.start2.services_and_responses

import com.example.start2.ApiResponse
import com.google.gson.JsonObject
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AddSongsBatchService {
    @POST("/api/add_songs_batch")
    suspend fun addSongsBatch(
        @Body request: JsonObject
    ): Response<AddSongsBatchResponse>
}

object AddSongsBatchServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.51.167.155/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: AddSongsBatchService by lazy {
        retrofit.create(AddSongsBatchService::class.java)
    }
}