package com.example.start2.home.Profile

data class UserFollowingsResponse(
    val success: Boolean,
    val follower_username: List<String>? = null,
    val followed_username : List<String>? = null,
    val errorMessage: String? = null,
    val message: String?
)