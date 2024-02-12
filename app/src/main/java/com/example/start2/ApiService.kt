package com.example.start2

import com.example.start2.home.Profile.FollowRequest
import com.example.start2.home.Profile.FollowResponse
import com.example.start2.home.Profile.ProfilePictureRequest
import com.example.start2.home.Profile.ProfilePictureResponse
import com.example.start2.home.Profile.UnfollowRequest
import com.example.start2.home.Profile.UserFollowingsRequest
import com.example.start2.home.Profile.UserFollowingsResponse
import com.example.start2.home.Profile.UserInfoRequest
import com.example.start2.home.Profile.UserProfile
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/user_info")
    suspend fun getUserProfile(@Body request: UserInfoRequest): UserProfile

    @POST("/api/unfollow")
    suspend fun unfollowUser(@Body request: UnfollowRequest): ApiResponse

    @POST("/api/follow")
    suspend fun followUser(@Body request: FollowRequest): FollowResponse


    @POST("/api/user_followings")
    suspend fun getUserFollowings(@Body request: UserFollowingsRequest): UserFollowingsResponse

    @POST("/api/profile_picture")
    suspend fun getProfilePicture(@Body request: ProfilePictureRequest): ProfilePictureResponse


}