package com.example.start2.services_and_responses

import com.example.start2.ProfileViewModel
import com.example.start2.viewmodels.Music


data class AddSongsBatchRequest(
    val username: String,
    val songs: List<Music>
)