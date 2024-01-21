package com.example.start2.auth

import android.content.Intent
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
class MainHostActivityTest {

    private lateinit var mainHostActivity: MainHostActivity
    private lateinit var bundle: Bundle  // Real instance, no @Mock annotation
    private lateinit var intent: Intent

    @Before
    fun setUp() {

    }
    fun testActivityLifecycle() {

        // Create the activity with the intent
        // Create an intent that your activity expects
        intent = Intent(Intent.ACTION_VIEW) // Use the appropriate intent
        mainHostActivity = Robolectric.buildActivity(MainHostActivity::class.java, intent)
            .create()
            .start()
            .resume()
            .get()

        // Now you can test your Activity
    }
    // Test cases for onLogin
    @Test
    fun testOnLogin_ValidUsername() {
        intent = Intent(Intent.ACTION_VIEW) // Use the appropriate intent
        mainHostActivity = Robolectric.buildActivity(MainHostActivity::class.java, intent)
            .create()
            .start()
            .resume()
            .get()

        val username = "validUsername"
        mainHostActivity.onLogin(username)
        verify(mainHostActivity).onLogin(username)
    }

    @Test
    fun testOnLogin_InvalidUsername() {
        intent = Intent(Intent.ACTION_VIEW) // Use the appropriate intent
        mainHostActivity = Robolectric.buildActivity(MainHostActivity::class.java, intent)
            .create()
            .start()
            .resume()
            .get()

        val username = ""
        mainHostActivity.onLogin(username)
        verify(mainHostActivity).onLogin(username)
    }

    // Test case for onSpotify
    // ...

    // Add more test cases as needed
}
