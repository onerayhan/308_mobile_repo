package com.example.start2

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import okio.Buffer

import java.io.BufferedReader
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONException


import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.JsonParser


class ProfileViewModel(private val usr: UserPreferences): ViewModel() {



    private val _username = MutableLiveData<String>(usr.username)
    val username: LiveData<String> = _username
    private val apiService = ApiManager.createApiService()

    // State for user profile
    private val _userProfile = mutableStateOf<UserProfile?>(null)
    val userProfile: State<UserProfile?> get() = _userProfile

    // State for loading and error handling
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    // Event to trigger navigation or other side effects
    private val _event = mutableStateOf<ProfileEvent?>(null)
    val event: State<ProfileEvent?> get() = _event


    // Define a data class to represent the user profile
    data class UserProfile(
        val username: String,
        val email: String,
        val followed_count: Int,
        val follower_count: Int,
        // Add more properties as needed
    )

    sealed class ProfileEvent {
        object NavigateToFollowers : ProfileEvent()
        // Add more events as needed
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                _loading.value = true
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username.value}")
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username}")

                val request = UserInfoRequest(username = _username.value.orEmpty())
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111 ${_username.value}")

                // Log statement before making the API request
                Log.d("UserProfile", "Fetching user profile for usernameBBBBBBB: ${(_username)}")

                val response = apiService.getUserProfile(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "User profile fetched successfully: $response")

                // Update the _userProfile state with the fetched data
                _userProfile.value = UserProfile(
                    username = response.username,
                    email = response.email,
                    follower_count= response.follower_count,
                    followed_count = response.followed_count
                    // Add more properties as needed
                )

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to fetch user profile", e)

                _error.value = "Failed to fetch user profile"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Fetch user profile operation completed")

                _loading.value = false
            }
        }
    }

    fun saveUsername(newUsername: String){
        Log.d("NavigatorActivity", "fetchUserProfile111 $newUsername")
        _username.value=newUsername
        Log.d("NavigatorActivity", "fetchUserProfile222 ${_username.value}")
        usr.username=newUsername
    }
    fun navigateToFollowers() {
        _event.value = ProfileEvent.NavigateToFollowers
    }

    // Reset the event after handling it
    fun onEventHandled() {
        _event.value = null
    }

    fun fetchUserProfileByUsername(friendUsername: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username.value}")
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111depedepedepe ${_username}")

                val request = UserInfoRequest(friendUsername)
                Log.d("NavigatorActivity", "fetchUserProfileaaa12111 ${_username.value}")

                // Log statement before making the API request
                Log.d("UserProfile", "Fetching user profile for usernameBBBBBBB: ${(_username)}")

                val response = apiService.getUserProfile(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "User profile fetched successfully: $response")

                // Update the _userProfile state with the fetched data
                _userProfile.value = UserProfile(
                    username = response.username,
                    email = response.email,
                    follower_count = response.follower_count,
                    followed_count = response.followed_count
                    // Add more properties as needed
                )

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to fetch user profile", e)

                _error.value = "Failed to fetch user profile"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Fetch user profile operation completed")

                _loading.value = false
            }
        }

    }
    fun unfollowUser(followedUsername: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val request = UnfollowRequest(_username.value.toString(), followedUsername)







                // Assuming UnfollowRequest is a data class representing the request body

                // Log statement before making the API request
                Log.d("UserProfile", "Unfollowing user:  $request)")

                val response = apiService.unfollowUser(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "Unfollow successful: $response")

                // You can handle the response if needed, for example, check for success or failure messages

                // Assuming the API response indicates success, you can update your UI or perform any other necessary actions

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to unfollow user", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to unfollow user"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Unfollow user operation completed")

                _loading.value = false
            }
        }
    }


    fun followUser(followedUsername: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val request = FollowRequest(_username.value.toString(), followedUsername)
                val requestJson = request.toJson()
                val itemsObject = JSONObject()
                itemsObject.put("followed_username", _username.value.toString())
                itemsObject.put("follower_username", followedUsername)
                val gson = Gson()
                val json = gson.toJson(mapOf(
                    "followed_username" to _username.value.orEmpty(),
                    "follower_username" to followedUsername,
                ))

                // Log statement before making the API request
                Log.d("UserProfile", "Following user: $json")

                val response = apiService.followUser(request )

                // Log statement after a successful API response
                Log.d("UserProfile", "Follow successful: $response")

                // You can handle the response if needed, for example, check for success or failure messages
                if (response.success) {
                    Log.d("UserProfile", "Follow successful: $response")
                } else {
                    // Handle failure, display error message, etc.
                    _error.value = response.message ?: "Failed to follow user"
                }

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to follow user", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to follow user"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Follow user operation completed")

                _loading.value = false
            }
        }
    }





    fun getUserFollowings() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val request = UserFollowingsRequest(_username.value.orEmpty())


                // Log statement before making the API request
                Log.d("UserProfile", "Fetching followings for user: $username")

                val response = apiService.getUserFollowings(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "User followings fetched successfully: $response")

                if (response.success) {
                    // Handle success, update UI with followers and followed users
                    val followers = response.followed_username ?: emptyList()
                    val followedUsers = response.follower_username ?: emptyList()
                    Log.d("UserProfile", "User followings fetched successfully12334: ${response.message}")

                    // Do something with the followers and followedUsers lists
                } else {
                    // Handle failure, display error message, etc.
                    _error.value = response.errorMessage ?: "Failed to retrieve user followings"
                }

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to retrieve user followings", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to retrieve user followings"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Retrieve user followings operation completed")

                _loading.value = false
            }
        }
    }

    fun fetchProfilePicture() {
        viewModelScope.launch {
            try {
                _loading.value = true

                val request = ProfilePictureRequest(username = _username.value.orEmpty())

                // Log statement before making the API request
                Log.d("UserProfile", "Fetching profile picture for username: ${_username.value}")

                val response = apiService.getProfilePicture(request)

                // Log statement after a successful API response
                Log.d("UserProfile", "Profile picture fetched successfully: $response")

                if (response.success) {
                    // Handle success, update UI with the profile picture
                    // For example, you can use the response.profilePicture ByteArray to display the image
                } else {
                    // Handle failure, display error message, etc.
                    _error.value = response.errorMessage ?: "Failed to retrieve profile picture"
                }

            } catch (e: Exception) {
                // Log statement in case of an exception
                Log.e("UserProfile", "Failed to fetch profile picture", e)

                // You can handle the error, for example, update the UI with an error message
                _error.value = "Failed to fetch profile picture"
            } finally {
                // Log statement after the try-catch block
                Log.d("UserProfile", "Fetch profile picture operation completed")

                _loading.value = false
            }
        }
    }

    fun uploadPhoto(photoFile: File) {
        val url = "http://51.20.128.164/api/upload_photo"

        // Launch a coroutine within viewModelScope to handle the network operation
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageBytes = photoFile.readBytes()
                val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                // Create a multipart request body with photo and username parameters
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("photo", "photo.jpg", RequestBody.create("image/jpeg".toMediaType(), base64Image.toByteArray()))
                    .addFormDataPart("username", _username.value.orEmpty())
                    .build()

                // Log the request payload
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                Log.d("ProfileViewModel", "Request Payload: ${buffer.readUtf8()}")
                Log.d("ProfileViewModel", "Request Payload12345: $requestBody")



                // Create a POST request
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                // Create an OkHttpClient
                val client = OkHttpClient()

                // Execute the request
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        // Handle the failure case
                        Log.d("ProfileViewModel", "Error: ${response.code} - ${response.message}")
                        Log.d("ProfileViewModel", response.body?.string() ?: "No response body")
                    } else {
                        // Handle the success case
                        Log.d("ProfileViewModel", "Success: ${response.body?.string()}")
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions, e.g., network issues
                Log.d("ProfileViewModel", "Exception: $e")
            }
        }
    }

    data class SongParams(
        val song_name: String,
        val username: String,
        val length: String? = null,
        val tempo: Int? = null,
        val recording_type: String? = null,
        val listens: Int? = null,
        val release_year: Int? = null,
        val added_timestamp: String? = null,
        val album_name: String? = null,
        val album_release_year: Int? = null,
        val performer_name: String? = null,
        val genre: String? = null,
        val mood: String? = null,
        val instrument: String? = null
    )



    fun addSong(songParams: SongParams) {
        viewModelScope.launch {
            try {
                _loading.value = true

                // Assuming Gson library for JSON serialization
                val gson = Gson()

                // Convert the SongParams object to JSON
                val json = gson.toJson(songParams)

                // Define the request body
                val requestBody = json.toRequestBody("application/json".toMediaType())
                Log.d("AddSongDebug", "Request Body1234: $json")


                // Define the request
                val request = Request.Builder()
                    .url("http://51.20.128.164/api/add_song")
                    .post(requestBody)
                    .build()

                // Create an OkHttpClient instance
                val client = OkHttpClient()

                // Execute the request asynchronously
                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        // Handle failure
                        Log.e("AddSongDebug", "Request failed", e)
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        // Handle the response as needed
                        val responseBody = response.body?.string()
                        Log.d("AddSongDebug", "Response Body: $responseBody")
                    }
                })

                // Log the request details for debugging
                Log.d("AddSongDebug", "Request URL: ${request.url}")
                Log.d("AddSongDebug", "Request Method: ${request.method}")
                Log.d("AddSongDebug", "Request Body: $json")
            } catch (e: Exception) {
                // Handle the exception (e.g., log, show error message)
                Log.e("AddSongDebug", "Exception: ${e.message}", e)
            } finally {
                // Cleanup or finalization
                _loading.value = false
            }
        }
    }

    data class RatingObject(
        val rating_type: String,
        val song_name: String?,
        val album_name: String?,
        val performer_name: String?,
        val rating: Int
    )

    data class UserRateBatchRequest(
        val username: String,
        val ratings: List<RatingObject>
    )

    fun postUserRateBatch(request: UserRateBatchRequest) {
        val gson = Gson()
        val json = gson.toJson(request)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val client = OkHttpClient()
        val url = "http://51.20.128.164/api/user_rate_batch"  // Replace with your actual API base URL
        val postRequest = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(postRequest).execute()

        if (!response.isSuccessful) {
            // Handle the error here
            println("Error: ${response.code} - ${response.message}")
        } else {
            // Handle the successful response here
            println("Success: ${response.body?.string()}")
        }
    }
    data class UserSongRatingRequest(
        val username: String,
        val song_name: String,
        val rating: Int
    )

    fun addUserSongRating(songId: Int, rating: Int) {

        GlobalScope.launch(Dispatchers.IO) {
            delay(3000)
            try {
                val gson = Gson()
                val json = gson.toJson(mapOf("username" to _username.value.toString(), "song_id" to songId, "rating" to rating))

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = json.toRequestBody(mediaType)

                val client = OkHttpClient()
                val url = "http://51.20.128.164/api/add_user_song_ratings"  // Replace with your actual API base URL
                val postRequest = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                Log.d("NetworkRequest", "Sending request to $url with JSON: $json")

                val response = client.newCall(postRequest).execute()

                if (response.isSuccessful) {
                    // Handle the successful response here
                    val responseBody = response.body?.string()
                    Log.d("NetworkRequest", "Success: $responseBody")
                } else {
                    // Handle other errors here
                    Log.e("NetworkRequest", "Error: ${response.code} - ${response.message}")
                }
            } catch (e: Exception) {
                // Handle exceptions here
                Log.e("NetworkRequest", "Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    fun addSongtr(songName: String, songId: String,rating: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val gson = Gson()
                val json = gson.toJson(mapOf("username" to _username.value.toString(), "song_name" to songName, "external_song_id" to songId))

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = json.toRequestBody(mediaType)

                val client = OkHttpClient()
                val url = "http://51.20.128.164/api/add_song"  // Replace with your actual API base URL
                val postRequest = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                Log.d("NetworkRequest", "Sending request to $url with JSON: $json")

                val response = client.newCall(postRequest).execute()

                if (response.isSuccessful) {
                    // Handle the successful response here
                    val responseBody = response.body?.string()
                    Log.d("NetworkRequest", "Success: $responseBody")
                    val jsonObject = JsonParser.parseString(responseBody).asJsonObject

                    // Extract values from the JSON object
                    val message = jsonObject.getAsJsonPrimitive("message").asString
                    val songId = jsonObject.getAsJsonPrimitive("song_id").asInt

                    // Now you can work with the extracted values
                    Log.d("NetworkRequest", "Message: $message")
                    Log.d("NetworkRequest", "Song ID: $songId")
                    addUserSongRating(songId,rating)




                } else {
                    // Handle other errors here
                    Log.e("NetworkRequest", "Error: ${response.code} - ${response.message}")
                }
            } catch (e: Exception) {
                // Handle exceptions here
                Log.e("NetworkRequest", "Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }



    data class UserAlbumRatingRequest(
        val username: String,
        val album_name: String,
        val rating: Int
    )
    fun addUserAlbumRating(username: String, albumName: String, rating: Int) {
        val gson = Gson()
        val json = gson.toJson(mapOf("username" to username, "album_name" to albumName, "rating" to rating))

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val client = OkHttpClient()
        val url = "http://51.20.128.164/api/user_album_ratings"  // Replace with your actual API base URL
        val postRequest = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(postRequest).execute()

        when {
            response.isSuccessful -> {
                // Handle the successful response here
                val responseBody = response.body?.string()
                println("Success: $responseBody")
            }
            response.code == 400 -> {
                // Handle Bad Request (Missing required parameters) here
                println("Bad Request: ${response.body?.string()}")
            }
            else -> {
                // Handle other errors here
                println("Error: ${response.code} - ${response.message}")
            }
        }
    }

    fun readContentFromUri(contentResolver: ContentResolver, uri: Uri, callback: (String) -> Unit) {
        Thread {
            val inputStream = contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = StringBuilder()
            var line: String?

            try {
                while (reader.readLine().also { line = it } != null) {
                    content.append(line).append('\n')
                }

                callback(content.toString())
            } catch (e: IOException) {
                // Handle IOException
                e.printStackTrace()
            } finally {
                try {
                    reader.close()
                    inputStream?.close()
                } catch (e: IOException) {
                    // Handle IOException
                    e.printStackTrace()
                }
            }
        }.start()
    }

    data class UserPerformerRatingRequest(
        val username: String,
        val performerName: String,
        val rating: Int
    )

    fun addUserPerformerRating(request: UserPerformerRatingRequest) {
        val gson = Gson()
        val json = gson.toJson(request)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val client = OkHttpClient()
        val url = "http://51.20.128.164/api/user_performer_ratings"  // Replace with your actual API base URL
        val postRequest = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(postRequest).execute()

            when {
                response.isSuccessful -> {
                    // Handle the successful response here
                    val responseBody = response.body?.string()
                    println("Success: $responseBody")
                }
                response.code == 400 -> {
                    // Handle Bad Request (Missing required parameters) here
                    println("Bad Request: ${response.body?.string()}")
                }
                else -> {
                    // Handle other errors here
                    println("Error: ${response.code} - ${response.message}")
                }
            }
        } catch (e: IOException) {
            // Handle network-related exceptions here
            println("Error: ${e.message}")
        } finally {
            // Close the OkHttpClient to release resources
            client.dispatcher.executorService.shutdown()
            client.connectionPool.evictAll()
        }
    }

    data class Song(
        val songName: String,
        val length: String?,
        val tempo: Int?,
        val recordingType: String?,
        val listens: Int?,
        val releaseYear: String?,
        val addedTimestamp: String?,
        val albumName: String?,
        val performerName: String?,
        val genre: String?,
        val mood: String?,
        val instrument: String?
    )

    data class AddSongsBatchRequest(
        val username: String,
        val songs: List<Song>
    )

    fun addSongsBatch(request: AddSongsBatchRequest) {
        val gson = Gson()
        val json = gson.toJson(request)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        val client = OkHttpClient()
        val url = "http://51.20.128.164/api/add_songs_batch"  // Replace with your actual API base URL
        val postRequest = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(postRequest).execute()

            when {
                response.isSuccessful -> {
                    // Handle the successful response here
                    val responseBody = response.body?.string()
                    println("Success: $responseBody")
                }
                response.code == 400 -> {
                    // Handle Bad Request (Missing required parameters) here
                    println("Bad Request: ${response.body?.string()}")
                }
                else -> {
                    // Handle other errors here
                    println("Error: ${response.code} - ${response.message}")
                }
            }
        } catch (e: IOException) {
            // Handle network-related exceptions here
            println("Error: ${e.message}")
        } finally {
            // Close the OkHttpClient to release resources
            client.dispatcher.executorService.shutdown()
            client.connectionPool.evictAll()
        }
    }

    private val _songList = MutableLiveData<List<com.example.start2.Song>>()
    val songList: LiveData<List<com.example.start2.Song>> get() = _songList



    fun fetchUserSongs() {
        GlobalScope.launch(Dispatchers.IO) {
            val apiUrl = "http://51.20.128.164/api/user_songs"

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                val jsonInputString = JSONObject().apply {
                    put("username", _username.value.orEmpty())
                }.toString()

                connection.doOutput = true
                connection.outputStream.use { os ->
                    os.write(jsonInputString.toByteArray(charset("utf-8")))
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()

                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    withContext(Dispatchers.Main) {
                        Log.d("SongViewModel", "API Response: $response")
                        parseSongsResponse(response.toString())
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val errorMessage = "Error: ${connection.responseMessage}"
                        Log.e("SongViewModel", errorMessage)
                        _error.value = errorMessage
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val errorMessage = "Error: ${e.message}"
                    Log.e("SongViewModel", errorMessage)
                    _error.value = errorMessage
                }
            }
        }
    }
    private fun parseSongsResponse(response: String) {
        try {
            val jsonArray = JSONArray(response)

            val songs = mutableListOf<com.example.start2.Song>()
            for (i in 0 until jsonArray.length()) {
                val songObject = jsonArray.getJSONObject(i)
                val song = Song(
                    title = songObject.getString("song_name"),
                    artist = songObject.getString("performer_name"),
                    album = songObject.getString("album_name"),
                    duration = songObject.getString("length")


                    // Add other properties as needed
                )
                songs.add(song)
            }

            _songList.value = songs
            Log.d("SongViewModel", "Parsed Songs: $songs")
        } catch (e: JSONException) {
            Log.e("SongViewModel", "Error parsing JSON: ${e.message}")
            _error.value = "Error parsing JSON: ${e.message}"
        }
    }






}




