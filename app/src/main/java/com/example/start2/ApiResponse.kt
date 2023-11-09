package com.example.start2


data class ApiResponse(
    val isSuccessful: Boolean,
    val responseCode: Int
) {
    val responseData: String
        get() {
            // Provide your custom implementation here
            return "Your custom response data"
        }
}