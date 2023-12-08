package com.example.start2.auth
import android.content.ContentValues.TAG
import android.content.Context
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.start2.ProfileViewModel
import com.example.start2.databinding.FragmentMainBinding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigation.findNavController
import com.example.start2.LoginListener
import com.example.start2.RegistrationActivity
import com.example.start2.RegistrationStepsListener
import com.example.start2.home.NavigatorActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.wait
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainFragment : Fragment() {

    private var listener: LoginListener? = null
    private lateinit var binding: FragmentMainBinding
    private val apiEndpoint =
        "http://51.20.128.164/api/get_all_users" // Replace with your API endpoint

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement RegistrationStepsListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            var username = binding.phoneNumberEditText.text.toString()


            GlobalScope.launch(Dispatchers.IO) {
                val isRegistered = isUsernameRegistered(username)
                launch(Dispatchers.Main) {
                    if (isRegistered) {
                        updateUIForRegisteredNumber()
                    } else {
                        navigateToRegistration(username)
                    }
                }
            }
        }



        binding.loginButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            val username = binding.phoneNumberEditText.text.toString().replace(" ", "")

            binding.backButton1.setOnClickListener {
                val navController = findNavController(view)
                navController.popBackStack()
            }

            val jsonObject = JSONObject().apply {
                put("username", username)
                put("password", password)
            }

            val apiUrl = "http://51.20.128.164/api/login" // Replace with your API endpoint
            Log.d("MainHost", "Spotify login started")

            CoroutineScope(Dispatchers.Main).launch {
                val spotifyCompleted = listener?.onSpotify() ?: false
                if(spotifyCompleted) {
                    performLogin(apiUrl,jsonObject,username)
                }
                else{
                    performLogin(apiUrl,jsonObject,username)

                }
            }


            //performLogin(apiUrl, jsonObject,username)
            Log.d("MainHost", "Spotify login started")



            // Use Kotlin coroutines to perform the network operation asynchronously

        }

    }

    private fun updateUIForRegisteredNumber() {
        binding.button.visibility = View.GONE
        binding.passwordEditText.visibility = View.VISIBLE
        binding.loginButton.visibility = View.VISIBLE
        binding.backButton1.visibility = View.VISIBLE

    }

    private fun navigateToRegistration(username: String) {
        val intent = Intent(requireContext(), RegistrationActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }


    private suspend fun performLogin(apiUrl: String, jsonRequestBody: JSONObject,username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                try {
                    val outputStream: OutputStream = connection.outputStream
                    val jsonBytes = jsonRequestBody.toString().toByteArray(Charsets.UTF_8)
                    outputStream.write(jsonBytes)
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        // Handle the error and show a user-friendly message
                    }
                    return@launch
                }

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    val responseBody = response.toString()

                    try {
                        val responseJsonArray = JSONArray(responseBody)

                        for (i in 0 until responseJsonArray.length()) {
                            val responseObject = responseJsonArray.getJSONObject(i)
                            val message = responseObject.getString("message")
                            val token = responseObject.getString("token")

                            // Handle 'message' and 'token' as needed
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            // Handle the error and show a user-friendly message
                        }
                    }

                    withContext(Dispatchers.Main) {
                        // Handle success and update the UI
                        Log.d(TAG, "login: $username")
                        Log.d(TAG,"Login successful. Response code: $responseCode")
                        listener?.onLogin(username)


                    }
                } else {
                    Log.d(TAG,"Login failed. Response code: $responseCode")
                    withContext(Dispatchers.Main) {
                        // Handle the error and show a user-friendly message

                    }
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    // Handle other exceptions and show a user-friendly message

                }
            }
        }
    }
    // Use the ComposeView to host the Composable function

    private fun isUsernameRegistered(username: String): Boolean {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$apiEndpoint?username=$username")
            .build()

        val TAG = "MainFragment"
        Log.d(TAG, "username test: $username")

        try {
            val response = client.newCall(request).execute()
            Log.d(TAG, response.code.toString())

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonArray = JSONArray(responseBody) // Parse as JSON array

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val registeredUsername = jsonObject.optString("username")
                    if (registeredUsername == username) {




                        return true
                    }
                }
            }
        } catch (e: IOException) {
            // Handle any network or API call errors
            e.printStackTrace()
        } catch (e: JSONException) {
            // Handle JSON parsing errors
            e.printStackTrace()
        }

        return false
    }
}
