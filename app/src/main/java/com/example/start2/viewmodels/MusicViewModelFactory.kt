package com.example.start2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.start2.home.spotify.SpotifyViewModel

class MusicViewModelFactory(private val username: String, private val isTest: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MusicViewModel(username, isTest) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}