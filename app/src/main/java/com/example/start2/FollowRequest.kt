package com.example.start2

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.moshi.Json
import org.json.JSONObject


data class FollowRequest(val follower_username: String, val followed_username: String) {
    fun toJson(): JSONObject {
        val gson = Gson()
        val jsonString = gson.toJson(this)
        return JSONObject(jsonString)
    }


}