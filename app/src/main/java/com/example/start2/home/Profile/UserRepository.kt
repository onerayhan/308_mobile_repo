package com.example.start2.home.Profile

/*
class UserRepository(private val apiService: ApiService) {

    suspend fun followUser(followerUsername: String, followedUsername: String): FollowResponse {
        val followRequest = FollowRequest(
            follower_username = followerUsername,
            followed_username = followedUsername
        )
        return apiService.followUser(followRequest)
    }

    suspend fun getUserFollowings(username: String): UserFollowingsResponse {
        // Make the API call using Retrofit or your preferred networking library
        val response = apiService.getUserFollowings(UserFollowingsRequest(username))

        // For simplicity, assuming that the response body is directly the UserFollowingsResponse
        return response.body() ?: throw Exception("Failed to get user followings")
    }



}

 */
