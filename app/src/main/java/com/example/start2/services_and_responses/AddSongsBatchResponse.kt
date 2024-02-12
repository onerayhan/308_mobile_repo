package com.example.start2.services_and_responses



data class AddSongsBatchResponse(
    val results: List<ResultMessage>?
)

data class ResultMessage(
    val message: String
)
