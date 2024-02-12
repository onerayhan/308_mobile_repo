package com.example.start2.home.Profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

/*
open class Friendwiewmodel : ViewModel() {

    private val _username = MutableLiveData<String>()
    var username: String
        get() = _username.value ?: ""
        set(value) {
            _username.value = value
        }
    private val apiService = ApiManager.createApiService()

    // State for user profile
    val _userProfile = mutableStateOf<UserProfile?>(null)
    val userProfile: State<UserProfile?> get() = _userProfile

    // State for loading and error handling
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    // Event to trigger navigation or other side effects
    private val _event = mutableStateOf<ProfileEvent?>(null)
    val event: State<ProfileEvent?> get() = _event

    // Define a data class to represent the user profile
    data class UserProfile(
        val username: String,
        val email: String,
        val followersCount: Int,
        val followingCount: Int,
        // Add more properties as needed
    )

    sealed class ProfileEvent {
        object NavigateToFollowers : ProfileEvent()
        // Add more events as needed
    }

    fun fetchUserProfile(friendUsername: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username.value}")
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username}")

                val request = UserInfoRequest(friendUsername)
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111 ${_username.value}")

                // Log statement before making the API request
                Log.d("UserProfile", "Fetching user profile for usernameBBBBBBB: ${(_username)}")

                val response = apiService.getUserProfile(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "User profile fetched successfully: $response")

                // Update the _userProfile state with the fetched data
                _userProfile.value = UserProfile(
                    username = response.username,
                    email = response.email,
                    follower_count = response.follower_count,
                    followed_count = response.followed_count
                    // Add more properties as needed
                )

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to fetch user profile", e)

                _error.value = "Failed to fetch user profile"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Fetch user profile operation completed")

                _loading.value = false
            }
        }
    }

    fun saveUsername(username: String){
        Log.d("NavigatorActivity", "fetchUserProfile111 $username")
        _username.value=username
        Log.d("NavigatorActivity", "fetchUserProfile222 ${_username.value}")
    }
    fun navigateToFollowers() {
        _event.value = ProfileEvent.NavigateToFollowers
    }

    // Reset the event after handling it
    fun onEventHandled() {
        _event.value = null
    }
}
*/