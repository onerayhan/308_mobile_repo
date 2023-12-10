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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

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
        intent.putExtra("SpotifyCode", registrationViewModel.tokenCode.value.toString())
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
                    registrationViewModel.saveTokenCode(response.code)
                    spotifyLoginResult.complete(true)

                    //exchangeCodeForToken(response.code.toString())

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


    private suspend fun exchangeCodeForToken(code: String) = withContext(Dispatchers.IO){
        val url = "https://accounts.spotify.com/api/token"
        val client = OkHttpClient()
        val REQUEST_CODE = 1337
        val REDIRECT_URI = "com.example.start2://callback"
        val CLIENT_ID = "214ab19a5a85486489db0ae512195fca"
        val CLIENT_SECRET = "118873683fc44590b3579c452bdcb3f1"

        val requestBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("code", code)
            .add("redirect_uri", REDIRECT_URI)
            .add("client_id", CLIENT_ID)
            .add("client_secret", CLIENT_SECRET)  // Warning: Including the client secret in your app is insecure
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                Log.d("MainHost", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("MainHost", responseBody.toString())
                response.body?.close()

                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    registrationViewModel.saveTokenData(jsonResponse)
                    //registrationViewModel.addMobileToken()
                    val accessToken = jsonResponse.getString("access_token")
                    val refreshToken = jsonResponse.getString("refresh_token")
                    // Store tokens securely and use them to access Spotify API
                } else {
                    // Handle error
                }
            }
        })
    }


}

