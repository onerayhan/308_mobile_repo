package com.example.start2

data class UserProfile(
    val username: String,
    val email: String,
    val followersCount: Int,
    val followingCount: Int,
    val favoriteSongs: List<String>
)




