package com.example.start2.home.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SpotifyViewModelFactory(private val token: String, private val isTest: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpotifyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpotifyViewModel(token, isTest) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


