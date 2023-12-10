package com.example.start2.services_and_responses

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface AddMobileTokenService {
    @POST("/api/add_mobile_token/{username}")
    suspend fun addMobileToken(
        @Path("username") username: String,
        @Body request: JsonObject
    ) : Response<AddMobileTokenResponse>
}


object AddMobileTokenServiceProvider {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://51.20.128.164/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val instance: AddMobileTokenService by lazy {
        retrofit.create(AddMobileTokenService::class.java)
    }
}
/*
Add Mobile Token
Request

Url /api/add_mobile_token/<username>
Method GET
Parameters

username: The username of the user.
access_token: Token used to access the user's spotify account.
refresh_token: Token used to refresh the access_token.
Description

Addition of token data*/