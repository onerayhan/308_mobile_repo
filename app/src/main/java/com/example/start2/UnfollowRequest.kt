package com.example.start2

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

data class UnfollowRequest(val follower_username: String, val followed_username: String) {
    fun toJson(): JsonObject {
        val gson = Gson()
        val jsonString = gson.toJson(this)
        return gson.fromJson(jsonString, JsonObject::class.java)
    }

}