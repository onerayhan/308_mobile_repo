package com.example.start2.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

import com.example.start2.home.screens.MainScreen
import com.example.start2.home.ui.theme.Guardians_of_codedevelopment_mobileTheme
import com.example.start2.home.spotify.DummySpotifyViewModel
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.spotify.SpotifyViewModelFactory

class NavigatorActivity : ComponentActivity() {

    private lateinit var viewModel: SpotifyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val token = intent.getStringExtra("SpotifyToken")
        // Initialize ViewModel
        val viewModel: SpotifyViewModel = if (token != null) {
            ViewModelProvider(this, SpotifyViewModelFactory(token)).get(SpotifyViewModel::class.java)
        } else {
            DummySpotifyViewModel("dummy")
        }
        // Fetch top tracks
        //viewModel.getUserTopTracks()
        setContent {
            Guardians_of_codedevelopment_mobileTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(viewModel)
            }
        }

    }
}