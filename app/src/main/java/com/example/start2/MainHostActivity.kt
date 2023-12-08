package com.example.start2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.start2.auth.MainFragment
import com.example.start2.home.NavigatorActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CompletableDeferred

class MainHostActivity : AppCompatActivity(), LoginListener {

    val REQUEST_CODE_MAIN_HOST = 1338
    val REDIRECT_URI = "com.example.start2:/callback"
    val CLIENT_ID = "214ab19a5a85486489db0ae512195fca"
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    private val spotifyLoginResult = CompletableDeferred<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_host)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_login, MainFragment())
                .commit()
        }
        /*registrationViewModel.spotifyToken.observe(this, Observer { token ->
            if(token != null) {

            }
        })*/

    }
    override fun onLogin(username: String) {
        val intent = Intent(this, NavigatorActivity::class.java)
        intent.putExtra("username", username)
        intent.putExtra( "SpotifyToken", registrationViewModel.spotifyToken.value.toString())
        startActivity(intent)
        finish()
    }


    override suspend fun onSpotify() : Boolean {
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        Log.d("MainHost", "Bura yok ?")
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
        //AuthorizationClient.stopLoginActivity(this, REQUEST_CODE)
        return spotifyLoginResult.await()
        Log.d("MainHost", "Opening Spotify login activity with request: $request")

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Log.d("MainHost", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE_MAIN_HOST) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            Log.d("A", "Spotify response: ${response.type}")
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d("MainHost", "RESPONSEUMDUR BU BENİM:  ${response.toString()}")

                    registrationViewModel.saveSpotifyToken(response.accessToken)
                    spotifyLoginResult.complete(true)
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e("A", "Spotify login error: ${response.error}")
                }
                else -> {
                    Log.d("A", "Other Spotify response type: ${response.type}")

                }
            }
        }
        spotifyLoginResult.complete(true)
    }


}
