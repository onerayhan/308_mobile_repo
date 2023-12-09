package com.example.start2

import com.example.start2.services_and_responses.AddSongsBatchRequest
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/user_info")
    suspend fun getUserProfile(@Body request: UserInfoRequest): UserProfile

    @POST("/api/unfollow")
    suspend fun unfollowUser(@Body request: JSONObject): ApiResponse

    @POST("/api/follow")
    suspend fun followUser(@Body request: JSONObject): FollowResponse

    @POST("/api/user_followings")
    suspend fun getUserFollowings(@Body request: UserFollowingsRequest): UserFollowingsResponse

    @POST("/api/profile_picture")
    suspend fun getProfilePicture(@Body request: ProfilePictureRequest): ProfilePictureResponse

}
