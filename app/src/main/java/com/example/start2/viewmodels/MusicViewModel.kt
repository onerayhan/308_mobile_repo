package com.example.start2.viewmodels

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.start2.ApiResponse
import com.example.start2.ApiService
import com.example.start2.services_and_responses.AddSongsBatchRequest
import com.example.start2.services_and_responses.AddSongsBatchResponse
import com.example.start2.services_and_responses.AddSongsBatchService
import com.example.start2.services_and_responses.AddSongsBatchServiceProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.indexOfFirstNonAsciiWhitespace
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

data class Music(
    val song_name: String,
    val length: String,
    val tempo: Int,
    val recording_type: String,
    val listens: Int,
    val release_year: String,
    val added_timestamp: String,
    val album_name: String,
    val performer_name: String,
    val genre: String,
    val mood: String,
    val instrument: String
)

data class MusicBatch(
    val songs: List<Music>
)
open class MusicViewModel(protected val username: String): ViewModel(){
    private val repository = MusicRepository(AddSongsBatchServiceProvider.instance)
    val parsedMusics = MutableLiveData<List<Music>>()
    val batchResult = MutableLiveData<Boolean>()

    fun saveSelectedMusics(musics: List<Music>) {
        val result = musics
        result?.let{
            parsedMusics.postValue(it)
        }
    }

    open fun postTracks(tracks: List<Music>) {
        viewModelScope.launch {
            val result = repository.postTracks(username, tracks)
            result?.let{
                Log.d("MusicViewModel", "-${it.results}")
                //Log.d("MusicViewModel", "-${it.isSuccessful}")

                batchResult.postValue(true)
            }
        }
    }
    fun processFileAndPostTracks(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            val fileContent = repository.readContentFromUri(contentResolver, uri)
            fileContent?.let{
                Log.d("MusicViewModel", "ALo: asdasd")
                val gson = Gson()
                val musicBatch = gson.fromJson(fileContent, MusicBatch::class.java)
                val musics = musicBatch.songs

                musics?.let {
                    Log.d("MusicViewModel", "-${musics[0].song_name}")
                    parsedMusics.postValue(it)
                    postTracks(it)
                    Log.d("MusicViewModel", "-${musics[0].song_name}")
                }
            //parseAndSaveMusics(it)
            }
            // Parse and process fileContent
            // Example: parseAndSaveMusics(fileContent)
            // postTracks()
        }
    }

    // In your MusicViewModel
    open fun parseAndSaveMusics(fileContent: String) {
        Log.d("MusicViewModel", "ALo: burada")
        val musics = fileContent.lines().filter { it.isNotBlank() }.map { line ->
            // Split each line and create Music objects
            // Assuming the format is exactly as the file content you provided
            // You need to modify the parsing logic based on the actual file format
            val parts = line.split(",")
            Log.d("MusicViewModel", "-${parts[0].substringAfter(" ")}")
            Music(
                song_name = parts[0].substringAfter(" "),
                length = parts[1].substringAfter(" "),
                tempo = parts[2].substringAfter(" ").toInt(),
                recording_type = parts[3].substringAfter(" "),
                listens = parts[4].substringAfter(" ").toInt(),
                release_year = parts[5].substringAfter(" "),
                added_timestamp = parts[6].substringAfter(" "),
                album_name = parts[7].substringAfter(" "),
                performer_name = parts[8].substringAfter(" "),
                genre = parts[9].substringAfter(" "),
                mood = parts[10].substringAfter(" "),
                instrument = parts[11].substringAfter(" ")
            )
        }
        Log.d("MusicViewModel", "ALo: burada???????")
        musics?.let{
            Log.d("MusicViewModel", "ALo: vb")
            parsedMusics.postValue(it)
            Log.d("MusicViewModel", "ALo: cv")
            postTracks(it)
            Log.d("MusicViewModel", "ALo: asd")
        }
    }


}

open class MusicRepository(private val addSongsBatchService: AddSongsBatchService) {
    open suspend fun postTracks(token: String, tracks: List<Music>) : AddSongsBatchResponse?{
        return try {

            val requestObject = AddSongsBatchRequest(token, tracks)
            val gson = Gson()
            val songsJsonArray = JSONArray(gson.toJson(requestObject.songs))
            val jsonObject = JsonObject()
            jsonObject.addProperty("username", requestObject.username)
            jsonObject.add("songs", gson.toJsonTree(requestObject.songs))
            val jsonString = gson.toJson(jsonObject)
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val last = jsonString.toRequestBody(mediaType)

            Log.d("MusicViewModel", "jsonString: ${jsonObject.toString()}")
            val response = addSongsBatchService.addSongsBatch(jsonObject)
            if(response.isSuccessful) {
                response.body()
            } else {
                AddSongsBatchResponse(results = null)
            }
        } catch (e:Exception) {
            AddSongsBatchResponse(results = null)
        }
    }

    open suspend fun readContentFromUri(contentResolver: ContentResolver, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val inputStream = contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = StringBuilder()
            var line: String?

            try {
                while (reader.readLine().also { line = it } != null) {
                    content.append(line).append('\n')
                }
            } finally {
                reader.close()
                inputStream?.close()
            }

            return@withContext content.toString()
        }
    }
}
