package com.example.start2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.start2.auth.MainFragment
import com.example.start2.home.NavigatorActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class MainHostActivity : AppCompatActivity(), LoginListener {

    val REQUEST_CODE = 1338
    val REDIRECT_URI = "com.example.start2:/callback"
    val CLIENT_ID = "214ab19a5a85486489db0ae512195fca"
    private val registrationViewModel by viewModels<RegistrationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_host)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_login, MainFragment())
                .commit()
        }

    }
    override fun onLogin(username: String) {
        intent = Intent(this, NavigatorActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra( "SpotifyToken", registrationViewModel.spotifyToken.value.toString())
        startActivity(intent)
        finish()
    }


    override fun onSpotify() {
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        Log.d("MainHost", "Spotify login started")
        //registrationViewModel.sendSpotifyRequestToMainServer()
        val REQUEST_CODE = 1338
        val REDIRECT_URI = "com.example.start2://callback"
        val CLIENT_ID = "214ab19a5a85486489db0ae512195fca"

        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("user-library-read,user-top-read"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
        // registrationViewModel.sendSpotifyIntent()

        Log.d("MainHost", "Opening Spotify login activity with request: $request")

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Log.d("MainHost", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            Log.d("A", "Spotify response: ${response.type}")
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("MainHost", "RESPONSEUMDUR BU BENİM:  ${response.toString()}")
                    // Starting NavigatorActivity
                    //val intent = Intent(this, NavigatorActivity::class.java)
                    //intent.putExtra("SpotifyToken", response.accessToken) // Passing the token to the next activity
                    //startActivity(intent)
                    registrationViewModel.saveSpotifyToken(response.accessToken)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("A", "Spotify login error: ${response.error}")
                }
                else -> {
                    Log.d("A", "Other Spotify response type: ${response.type}")

                }
            }
        }
    }


}
