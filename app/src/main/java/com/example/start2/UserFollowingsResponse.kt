package com.example.start2

data class UserFollowingsResponse(
    val success: Boolean,
    val followers: List<String>? = null,
    val followedUsers: List<String>? = null,
    val errorMessage: String? = null
)