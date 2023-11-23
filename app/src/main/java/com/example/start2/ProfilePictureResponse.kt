package com.example.start2
data class ProfilePictureResponse(
    val success: Boolean,
    val errorMessage: String? = null,
    val profilePicture: ByteArray? = null
)
