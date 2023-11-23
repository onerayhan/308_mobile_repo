package com.example.start2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class ProfilePictureViewModel : ViewModel() {

    private val _profilePicture = MutableLiveData<Bitmap>()
    val profilePicture: LiveData<Bitmap> get() = _profilePicture

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchProfilePicture(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val apiUrl = "http://13.51.167.155/api/profile_picture"

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                // Create JSON object with the username
                val jsonInputString = "{\"username\":\"$username\"}"

                // Send the POST request
                connection.doOutput = true
                connection.outputStream.use { os ->
                    os.write(jsonInputString.toByteArray(charset("utf-8")))
                }

                // Get the response
                val responseCode = connection.responseCode
                Log.d("ProfilePictureViewModel", "Response Code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the image from the input stream
                    val inputStream: BufferedInputStream = BufferedInputStream(connection.inputStream)
                    val bitmap = Bitmap.createBitmap(BitmapFactory.decodeStream(inputStream))

                    _profilePicture.postValue(bitmap)
                    Log.d("ProfilePictureViewModel", "Profile picture fetched successfully")
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    val errorMessage = "Error: Profile picture not found for user $username"
                    Log.e("ProfilePictureViewModel", errorMessage)
                    _error.postValue(errorMessage)
                } else {
                    val errorMessage = "Error: ${connection.responseMessage}"
                    Log.e("ProfilePictureViewModel", errorMessage)
                    _error.postValue(errorMessage)
                }

            } catch (e: Exception) {
                val errorMessage = "Error: ${e.message}"
                Log.e("ProfilePictureViewModel", errorMessage)
                _error.postValue(errorMessage)
            }
        }
    }
}
