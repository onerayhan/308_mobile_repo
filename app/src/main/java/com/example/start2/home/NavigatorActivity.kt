package com.example.start2.home

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.start2.ProfileViewModel
import com.example.start2.R
import com.example.start2.UserPreferences
import com.example.start2.home.screens.MainScreen
import com.example.start2.home.ui.theme.Guardians_of_codedevelopment_mobileTheme
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.spotify.SpotifyViewModelFactory
import com.example.start2.home.spotify.DummySpotifyViewModel

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.start2.viewmodels.DummyMusicViewModel
import com.example.start2.viewmodels.MusicViewModel
import com.example.start2.viewmodels.MusicViewModelFactory

class NavigatorActivity : ComponentActivity() {
    private val pv by viewModels<ProfileViewModel>()

    private lateinit var viewModel: SpotifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        actionBar?.hide()

        val username = intent.getStringExtra("username")
        val  userPreferences = UserPreferences(this)
        userPreferences.username= username
        val token = intent.getStringExtra("SpotifyToken")
        val code = intent.getStringExtra("SpotifyCode")
        if (token != null) {
            Log.d("token", token)

        }else{
            Log.d("token", "alo")
        }
        // Initialize ViewModel
        val viewModel: SpotifyViewModel = if (token != null) {
            ViewModelProvider(this, SpotifyViewModelFactory(token)).get(SpotifyViewModel::class.java)
        } else {
            DummySpotifyViewModel("dummy")
        }
        val musicViewModel: MusicViewModel = if (username != null) {
            ViewModelProvider(this, MusicViewModelFactory(username)).get(MusicViewModel::class.java)
        } else {
            DummyMusicViewModel("dummy")
        }

        viewModel.saveTokenCode(code)
        viewModel.saveUsername(username!!)
        // Fetch top tracks
        //viewModel.getUserTopTracks()
        setContent {
            Guardians_of_codedevelopment_mobileTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(viewModel, musicViewModel)
            }
        }

    }
}