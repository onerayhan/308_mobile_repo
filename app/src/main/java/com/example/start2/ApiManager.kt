package com.example.start2



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private const val BASE_URL = "http://51.20.128.164/" // Replace with your API base URL

    fun createApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}