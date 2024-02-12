package com.example.start2.auth

interface LoginListener {
    suspend fun onSpotify(): Boolean
    fun onLogin(username: String)
}