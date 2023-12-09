package com.example.start2

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.screens.HomeScreen
import com.example.start2.viewmodels.MusicViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


val navController = mock(NavController::class.java)


class FakeMusicViewModel : MusicViewModel("oneryhan") {
    // override members and functions as needed for testing
}


@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    val musicViewModel = FakeMusicViewModel()
    @Test
    fun homeScreen_displaysCorrectText() {
        // Set up your test scenario
        composeTestRule.setContent {
            HomeScreen(navController =  navController, musicViewModel =  musicViewModel, showDetail = {
            })
        }

        // Assert that the HomeScreen displays the expected text
        composeTestRule
            .onNodeWithText("Add Music")
            .assertExists()
    }
}
