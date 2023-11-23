package com.example.start2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


/*
class FollowViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _followResult = MutableLiveData<FollowResult>()
    val followResult: LiveData<FollowResult>
        get() = _followResult

    fun followUser(followerUsername: String, followedUsername: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.followUser(followerUsername, followedUsername)
                if (response.success) {
                    _followResult.value = FollowResult.Success("Relationship added successfully.")
                } else {
                    _followResult.value = FollowResult.Failure(response.message)
                }
            } catch (e: Exception) {
                _followResult.value = FollowResult.Failure("Failed to follow user.")
            }
        }
    }
}

sealed class FollowResult {
    data class Success(val message: String) : FollowResult()
    data class Failure(val errorMessage: String) : FollowResult()
}

 */
