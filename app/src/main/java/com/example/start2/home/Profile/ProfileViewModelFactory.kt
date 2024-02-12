package com.example.start2.home.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.start2.home.Profile.ProfileViewModel

class ProfileViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}