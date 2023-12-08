package com.example.start2

interface LoginListener {
    suspend fun onSpotify(): Boolean
    fun onLogin(username: String)
}