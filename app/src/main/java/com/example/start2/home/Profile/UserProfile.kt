package com.example.start2.home.Profile

data class UserProfile(
    val username: String,
    val email: String,
    val followed_count: Int,
    val follower_count: Int,
    val favoriteSongs: List<String>
)





