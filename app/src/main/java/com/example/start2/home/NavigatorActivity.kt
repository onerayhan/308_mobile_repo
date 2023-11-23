package com.example.start2.home
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.start2.ProfileViewModel
import com.example.start2.UserPreferences

import com.example.start2.home.screens.MainScreen
import com.example.start2.home.ui.theme.Guardians_of_codedevelopment_mobileTheme

class NavigatorActivity : ComponentActivity() {

    private val pv by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        val username = intent.getStringExtra("username")
        val  userPreferences = UserPreferences(this)
        userPreferences.username= username


        setContent {
            Guardians_of_codedevelopment_mobileTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }

    }

}