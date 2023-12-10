package com.example.start2

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.start2.auth.BirthdayStepFragment
import com.example.start2.home.NavigatorActivity
import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.security.MessageDigest
import java.security.SecureRandom



class RegistrationActivity : AppCompatActivity(), RegistrationStepsListener{

    private val TAG = "RegistrationActivity"
    val REQUEST_CODE = 1337
    val REDIRECT_URI = "com.example.start2:/callback"
    val CLIENT_ID = "214ab19a5a85486489db0ae512195fca"
    private val registrationViewModel by viewModels<RegistrationViewModel>()
    val CODE_VERIFIER = getCodeVerifier()


    private fun getCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val code = ByteArray(64)
        secureRandom.nextBytes(code)
        return Base64.encodeToString(
            code,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    fun getCodeChallenge(verifier: String): String {
        val bytes = verifier.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest = messageDigest.digest()
        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }
    /*
    fun getLoginActivityCodeIntent(): Intent =
        AuthorizationClient.createLoginActivityIntent(
            activity,
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI)
                .setScopes(
                    arrayOf(
                        "user-library-read", "user-library-modify",
                        "app-remote-control", "user-read-currently-playing"
                    )
                )
                .setCustomParam("code_challenge_method", "S256")
                .setCustomParam("code_challenge", getCodeChallenge(CODE_VERIFIER))
                .build()
        )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val username = intent.getStringExtra("username")
        username?.let {
            registrationViewModel.saveUsername(it)
        }

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {


            if (supportFragmentManager.findFragmentById(R.id.fragment_container) is BirthdayStepFragment) {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                finish() // Eğer com.example.start2.auth.MainFragment Activity'nin bir parçası değilse Activity'yi kapat
            } else {
                supportFragmentManager.popBackStack()
            }
        }

        Log.d(TAG, "onCreate called")
        /*
        // changes with the response will be made here
        registrationViewModel.response.observe(this, Observer { responseBody ->
            // Logic to handle the response
            // If the response meets certain conditions, start a new activity
            if (responseBody != "Req") {
                val intent = Intent(this, NavigatorActivity::class.java)
                // Optionally, you can pass data to the new activity
                intent.putExtra("responseKey", responseBody)
                //intent.putExtra("SpotifyToken", token)
                registrationViewModel.spotifyToken.value?.let {token ->
                    intent.putExtra("SpotifyToken", token)

                }
                startActivity(intent)
                finish()  // If you want to close the current

            }
        })*/
        registrationViewModel.combinedData.observe(this, Observer { (responseBody, token) ->
            if (responseBody != null && responseBody != "Req" && token != null) {
                val intent = Intent(this, NavigatorActivity::class.java)
                intent.putExtra("responseKey", responseBody)
                intent.putExtra("SpotifyToken", token)
                startActivity(intent)
                finish()
            }
        })


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BirthdayStepFragment())
                .addToBackStack(null)
                .commit()

            Log.d(TAG, "Initial fragment (BirthdayStepFragment) is set")
        }



    }


    override fun onBirthdaySelected(birthday: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UsernameStepFragment())
            .addToBackStack(null)
            .commit()
        Log.d(TAG, "DOĞUM GÜNÜMDÜ: $birthday")

        registrationViewModel.saveBirthday(birthday)

    }

    override fun onEmailSelected(email: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PasswordStepFragment())
            .addToBackStack(null)
            .commit()
        Log.d(TAG, "MAİLİMDİR BU BENİM:  $email")


        registrationViewModel.saveEmail(email)
    }

    override fun onPasswordSelected(password: String) {
        registrationViewModel.savePassword(password)
            Log.d(TAG, "PASSWORDUMDUR BU BENİM:  $password")

        registrationViewModel.sendUserDataToApi()

    }

    override fun onSpotifySelected() {
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        Log.d(TAG, "Spotify login started")
        //registrationViewModel.sendSpotifyRequestToMainServer()
        val REQUEST_CODE = 1337
        val REDIRECT_URI = "com.example.start2://callback"
        val CLIENT_ID = "214ab19a5a85486489db0ae512195fca"

        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("user-library-read,user-top-read"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)
       // registrationViewModel.sendSpotifyIntent()

        Log.d(TAG, "Opening Spotify login activity with request: $request")

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        Log.d(TAG, "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            Log.d(TAG, "Spotify response: ${response.type}")
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d(TAG, "RESPONSEUMDUR BU BENİM:  ${response.toString()}")
                    registrationViewModel.saveSpotifyToken(response.accessToken)

                    //registrationViewModel.saveRefreshToken(response.)
                }
                AuthorizationResponse.Type.CODE -> {

                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e(TAG, "Spotify login error: ${response.error}")
                }
                else -> {
                    Log.d(TAG, "Other Spotify response type: ${response.type}")

                }
            }
        }
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == AUTH_REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    // Handle successful response
                    val accessToken = response.accessToken
                }
                AuthenticationResponse.Type.ERROR -> {
                    // Handle error response
                }
                else -> {
                    // Handle other cases
                }
            }
        }
    }
*/
}

/*
        val builder =
            AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.CODE, REDIRECT_URI)

        builder.setScopes(arrayOf("user-library-read"))
        val request = builder.build()

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            // Check if result comes from the correct activity
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val resultCode = result.resultCode
                val data = result.data
                val response: AuthorizationResponse = AuthorizationClient.getResponse(resultCode, data)
                when (response.type) {
                    // Response was successful and contains auth token
                    AuthorizationResponse.Type.TOKEN -> {
                        println("Success! ${AuthorizationResponse.Type.TOKEN}")
                    }
                    // Auth flow returned an error
                    AuthorizationResponse.Type.ERROR -> {
                        println("Error")
                        println(AuthorizationResponse.Type.ERROR)
                    }
                    // Most likely auth flow was cancelled
                    else -> {
                        println("Auth flow canceled")
                    }
                }
            } else {
                println("No result returned")
            }
        }*/


private fun exchangeCodeForToken(code: String) {
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
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            response.body?.close()

            if (response.isSuccessful && responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                val accessToken = jsonResponse.getString("access_token")
                val refreshToken = jsonResponse.getString("refresh_token")
                // Store tokens securely and use them to access Spotify API
            } else {
                // Handle error
            }
        }
    })
}