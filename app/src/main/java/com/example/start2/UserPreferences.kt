package com.example.start2

import android.content.Context

class UserPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var username: String?
        get() = sharedPreferences.getString("username", null)
        set(value) = sharedPreferences.edit().putString("username", value).apply()

}