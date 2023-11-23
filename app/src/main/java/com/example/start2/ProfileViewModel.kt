package com.example.start2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.start2.ApiManager
import com.example.start2.ApiService
import kotlinx.coroutines.launch
import com.example.start2.UserInfoRequest

import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class ProfileViewModel(private val usr: UserPreferences): ViewModel() {



    private val _username = MutableLiveData<String>(usr.username)
    val username: LiveData<String> = _username
    private val apiService = ApiManager.createApiService()

    // State for user profile
    private val _userProfile = mutableStateOf<UserProfile?>(null)
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

    fun fetchUserProfile() {


        viewModelScope.launch {
            try {
                _loading.value = true
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username.value}")
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username}")

                val request = UserInfoRequest(username = _username.value.orEmpty())
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
                    followersCount = response.followersCount,
                    followingCount = response.followingCount
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

    fun saveUsername(newUsername: String){
        Log.d("NavigatorActivity", "fetchUserProfile111 $newUsername")
        _username.value=newUsername
        Log.d("NavigatorActivity", "fetchUserProfile222 ${_username.value}")
        usr.username=newUsername
    }
    fun navigateToFollowers() {
        _event.value = ProfileEvent.NavigateToFollowers
    }

    // Reset the event after handling it
    fun onEventHandled() {
        _event.value = null
    }

    fun fetchUserProfileByUsername(friendUsername: String) {
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
                    followersCount = response.followersCount,
                    followingCount = response.followingCount
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
    fun unfollowUser(followerUsername: String, followedUsername: String) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val request = UnfollowRequest(followerUsername, followedUsername)
                // Assuming UnfollowRequest is a data class representing the request body

                // Log statement before making the API request
                Log.d("UserProfile", "Unfollowing user: $followerUsername -> $followedUsername")

                val response = apiService.unfollowUser(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "Unfollow successful: $response")

                // You can handle the response if needed, for example, check for success or failure messages

                // Assuming the API response indicates success, you can update your UI or perform any other necessary actions

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to unfollow user", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to unfollow user"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Unfollow user operation completed")

                _loading.value = false
            }
        }
    }
    fun followUser(followerUsername: String, followedUsername: String) {
        viewModelScope.launch {
            try {
                _loading.value = true

                val request = FollowRequest(followerUsername, followedUsername)

                // Log statement before making the API request
                Log.d("UserProfile", "Following user: $followerUsername -> $followedUsername")

                val response = apiService.followUser(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "Follow successful: $response")

                // You can handle the response if needed, for example, check for success or failure messages
                if (response.success) {
                    // Handle success, update UI, etc.
                } else {
                    // Handle failure, display error message, etc.
                    _error.value = response.message ?: "Failed to follow user"
                }

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to follow user", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to follow user"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Follow user operation completed")

                _loading.value = false
            }
        }
    }
    fun getUserFollowings() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val request = UserFollowingsRequest(_username.value.orEmpty())

                // Log statement before making the API request
                Log.d("UserProfile", "Fetching followings for user: $username")

                val response = apiService.getUserFollowings(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "User followings fetched successfully: $response")

                if (response.success) {
                    // Handle success, update UI with followers and followed users
                    val followers = response.followers ?: emptyList()
                    val followedUsers = response.followedUsers ?: emptyList()

                    // Do something with the followers and followedUsers lists
                } else {
                    // Handle failure, display error message, etc.
                    _error.value = response.errorMessage ?: "Failed to retrieve user followings"
                }

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to retrieve user followings", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to retrieve user followings"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Retrieve user followings operation completed")

                _loading.value = false
            }
        }
    }

    fun fetchProfilePicture() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val request = ProfilePictureRequest(username = _username.value.orEmpty())

                // Log statement before making the API request
                Log.d("UserProfile", "Fetching profile picture for username: ${_username.value}")

                val response = apiService.getProfilePicture(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "Profile picture fetched successfully: $response")

                if (response.success) {
                    // Handle success, update UI with the profile picture
                    // For example, you can use the response.profilePicture ByteArray to display the image
                } else {
                    // Handle failure, display error message, etc.
                    _error.value = response.errorMessage ?: "Failed to retrieve profile picture"
                }

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to fetch profile picture", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to fetch profile picture"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Fetch profile picture operation completed")

                _loading.value = false
            }
        }
    }




}
