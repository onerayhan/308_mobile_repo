package com.example.start2

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/*
class UserFollowingsViewModel(private val repository: UserRepository) : ViewModel() {

    val userFollowingsData = MutableLiveData<UserFollowingsResponse>()
    val errorData = MutableLiveData<String>()

    fun getUserFollowings(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching user followings for username: $username")
                val response = repository.getUserFollowings(username)
                if (response != null) {
                    Log.d(TAG, "User followings successfully retrieved")
                    userFollowingsData.postValue(response)
                } else {
                    val errorMessage = "User followings response is null"
                    Log.d(TAG, errorMessage)
                    errorData.postValue(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Failed to get user followings: ${e.message}"
                Log.d(TAG, errorMessage)
                errorData.postValue(errorMessage)
            }
        }
    }

    companion object {
        private const val TAG = "UserFollowingsViewModel"
    }
}
*/