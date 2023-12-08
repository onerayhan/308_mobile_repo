package com.example.start2

// SongViewModel.kt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SongViewModel : ViewModel() {

    private val _songList = MutableLiveData<List<Song>>()
    val songList: LiveData<List<Song>> get() = _songList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchUserSongs(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val apiUrl = "http://51.20.128.164/api/user_songs"

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                val jsonInputString = JSONObject().apply {
                    put("username", username)
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

            val songs = mutableListOf<Song>()
            for (i in 0 until jsonArray.length()) {
                val songObject = jsonArray.getJSONObject(i)
                val song = Song(
                    title = songObject.getString("title"),
                    artist = songObject.getString("artist"),
                    album = songObject.getString("album")
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
