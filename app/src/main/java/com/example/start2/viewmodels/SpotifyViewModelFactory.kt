package com.example.start2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SpotifyViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpotifyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SpotifyViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


