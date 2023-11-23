package com.example.start2

data class UnfollowRequest(
    val follower_username: String,
    val followed_username: String
)