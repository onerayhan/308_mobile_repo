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
import com.example.start2.services_and_responses.AllAlbumPreferencesService
import com.example.start2.services_and_responses.AllAlbumPreferencesServiceProvider
import com.example.start2.services_and_responses.AllGenrePreferencesService
import com.example.start2.services_and_responses.AllGenrePreferencesServiceProvider
import com.example.start2.services_and_responses.AllPerformerPreferencesService
import com.example.start2.services_and_responses.AllPerformerPreferencesServiceProvider
import com.example.start2.services_and_responses.GroupAlbumPreferencesService
import com.example.start2.services_and_responses.GroupAlbumPreferencesServiceProvider
import com.example.start2.services_and_responses.GroupGenrePreferencesService
import com.example.start2.services_and_responses.GroupGenrePreferencesServiceProvider
import com.example.start2.services_and_responses.GroupPerformerPreferencesService
import com.example.start2.services_and_responses.GroupPerformerPreferencesServiceProvider
import com.example.start2.services_and_responses.RecommendationsResponse
import com.example.start2.services_and_responses.RecommendationsService
import com.example.start2.services_and_responses.RecommendationsServiceProvider
import com.example.start2.services_and_responses.UserAlbumPreferencesResponse
import com.example.start2.services_and_responses.UserAlbumPreferencesService
import com.example.start2.services_and_responses.UserAlbumPreferencesServiceProvider
import com.example.start2.services_and_responses.UserFollowingsAlbumPreferencesService
import com.example.start2.services_and_responses.UserFollowingsAlbumPreferencesServiceProvider
import com.example.start2.services_and_responses.UserFollowingsGenrePreferencesResponse
import com.example.start2.services_and_responses.UserFollowingsGenrePreferencesService
import com.example.start2.services_and_responses.UserFollowingsGenrePreferencesServiceProvider
import com.example.start2.services_and_responses.UserFollowingsPerformerPreferencesService
import com.example.start2.services_and_responses.UserFollowingsPerformerPreferencesServiceProvider
import com.example.start2.services_and_responses.UserGenrePreferencesResponse
import com.example.start2.services_and_responses.UserGenrePreferencesService
import com.example.start2.services_and_responses.UserGenrePreferencesServiceProvider
import com.example.start2.services_and_responses.UserGetAlbumRatingsResponse
import com.example.start2.services_and_responses.UserGetAlbumRatingsService
import com.example.start2.services_and_responses.UserGetAlbumRatingsServiceProvider
import com.example.start2.services_and_responses.UserGetPerformerRatingsResponse
import com.example.start2.services_and_responses.UserGetPerformerRatingsService
import com.example.start2.services_and_responses.UserGetPerformerRatingsServiceProvider
import com.example.start2.services_and_responses.UserGetSongRatingsResponse
import com.example.start2.services_and_responses.UserGetSongRatingsService
import com.example.start2.services_and_responses.UserGetSongRatingsServiceProvider
import com.example.start2.services_and_responses.UserPerformerPreferencesResponse
import com.example.start2.services_and_responses.UserPerformerPreferencesService
import com.example.start2.services_and_responses.UserPerformerPreferencesServiceProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
open class MusicViewModel(protected val username: String, protected val isTest: Boolean): ViewModel(){
    private var repository = when(isTest){
        true -> {
            MockMusicRepository()
        }
        false -> {
            MusicRepository(AddSongsBatchServiceProvider.instance, UserGenrePreferencesServiceProvider.instance, UserAlbumPreferencesServiceProvider.instance,
                UserPerformerPreferencesServiceProvider.instance, UserGetSongRatingsServiceProvider.instance, UserGetAlbumRatingsServiceProvider.instance, UserGetPerformerRatingsServiceProvider.instance,
                GroupGenrePreferencesServiceProvider.instance, GroupAlbumPreferencesServiceProvider.instance, GroupPerformerPreferencesServiceProvider.instance,
                UserFollowingsGenrePreferencesServiceProvider.instance,UserFollowingsAlbumPreferencesServiceProvider.instance,
                UserFollowingsPerformerPreferencesServiceProvider.instance, AllGenrePreferencesServiceProvider.instance,AllAlbumPreferencesServiceProvider.instance,
                AllPerformerPreferencesServiceProvider.instance, RecommendationsServiceProvider.instance,
                )
        }

    }
    fun setRepository(repo: IMusicRepository) {
        repository = repo
    }
    val parsedMusics = MutableLiveData<List<Music>>()
    val batchResult = MutableLiveData<Boolean>()

    val userGenrePreferences = MutableLiveData<UserGenrePreferencesResponse>()
    val userPerformerPreferences = MutableLiveData<UserPerformerPreferencesResponse>()
    val userAlbumPreferences = MutableLiveData<UserAlbumPreferencesResponse>()

    val userSongRatings = MutableLiveData<UserGetSongRatingsResponse>()
    val userAlbumRatings = MutableLiveData<UserGetAlbumRatingsResponse>()
    val userPerformerRatings = MutableLiveData<UserGetPerformerRatingsResponse>()
    val recommendationResults = MutableLiveData<RecommendationsResponse>()


    private val options = listOf(
        "MyGenrePrefs",
        "MyAlbumPrefs",
        "MyPerformerPrefs",
        "FollowingsGenrePrefs",
        "FollowingsAlbumPrefs",
        "FollowingsPerformerPrefs",
        "GroupGenrePrefs",
        "GroupAlbumPrefs",
        "GroupPerformerPrefs",
        "AllGenrePrefs",
        "AllAlbumPrefs",
        "AllPerformerPrefs")
    private var selectedOptions = mutableSetOf<String>()



    fun onOptionSelected(options: List<String>) {
        options.forEach{ option ->
            if (selectedOptions.contains(option)) {
                selectedOptions.remove(option)
            } else {
                selectedOptions.add(option)
            }
        }
        fetchPreferencesBasedOnSelection()
    }

    //Since only one genre can be active at a  moment we dont need to wait for more than 3 responses and all of their responses are of the same type
    private fun fetchPreferencesBasedOnSelection() {
        viewModelScope.launch {
            val deferredResponses = selectedOptions.map { option ->
                when (option) {
                    "MyGenrePrefs" -> async { repository.getUserGenrePreferences(username) }
                    "MyAlbumPrefs" -> async { repository.getUserAlbumPreferences(username) }
                    "MyPerformerPrefs" -> async { repository.getUserPerformerPreferences(username) }
                    "FollowingsGenrePrefs" -> async { repository.getUserFollowingsGenrePreferences(username) }
                    "FollowingsAlbumPrefs" -> async { repository.getUserFollowingsAlbumPreferences(username) }
                    "FollowingsPerformerPrefs" -> async { repository.getUserFollowingsPerformerPreferences(username) }
                    "GroupGenrePrefs" -> async { repository.getGroupGenrePreferences(username) }
                    "GroupAlbumPrefs" -> async { repository.getGroupAlbumPreferences(username) }
                    "GroupPerformerPrefs" -> async { repository.getGroupPerformerPreferences(username) }
                    "AllGenrePrefs" -> async { repository.getAllGenrePreferences() }
                    "AllAlbumPrefs" -> async { repository.getAllAlbumPreferences() }
                    "AllPerformerPrefs" -> async { repository.getAllPerformerPreferences() }

                    else -> async { null }
                }
            }
            deferredResponses.forEach { deferred ->
                val response = deferred.await()
                when (response) {
                    is UserGenrePreferencesResponse -> response?.let { userGenrePreferences.postValue(it) }
                    is UserAlbumPreferencesResponse -> response?.let { userAlbumPreferences.postValue(it) }
                    is UserPerformerPreferencesResponse -> response?.let { userPerformerPreferences.postValue(it) }
                }
            }
        }
    }

    open fun getRecommendation(criteriaString: String,targetAudience: String ){
        viewModelScope.launch {

            val result = repository.getRecommendationsFromDB(username,criteriaString, targetAudience)
            result?.let{
                recommendationResults.postValue(it)
            }
        }
    }
    open fun saveSelectedMusics(musics: List<Music>) {
        val result = musics
        result?.let{
            parsedMusics.postValue(it)
        }
    }

    open fun getUserGenrePreferences() {
        viewModelScope.launch{
            val result = repository.getUserGenrePreferences(username)
            result?.let{
                userGenrePreferences.postValue(it)
            }
        }
    }
    open fun getUserAlbumPreferences() {
        viewModelScope.launch{
            val result = repository.getUserAlbumPreferences(username)
            result?.let{
                userAlbumPreferences.postValue(it)
            }
        }
    }

    open fun getUserPerformerPreferences() {
        viewModelScope.launch{
            val result = repository.getUserPerformerPreferences(username)
            result?.let{
                userPerformerPreferences.postValue(it)
            }
        }
    }
    open fun getGroupGenrePreferences() {
        viewModelScope.launch{
            val result = repository.getGroupGenrePreferences(username)
            result?.let{
                userGenrePreferences.postValue(it)
            }
        }
    }
    open fun getGroupAlbumPreferences() {
        viewModelScope.launch{
            val result = repository.getGroupAlbumPreferences(username)
            result?.let{
                userAlbumPreferences.postValue(it)
            }
        }
    }
    open fun getGroupPerformerPreferences() {
        viewModelScope.launch{
            val result = repository.getGroupPerformerPreferences(username)
            result?.let{
                userPerformerPreferences.postValue(it)
            }
        }
    }
    open fun getAllGenrePreferences() {
        viewModelScope.launch{
            val result = repository.getAllGenrePreferences()
            result?.let{
                userGenrePreferences.postValue(it)
            }
        }
    }
    open fun getAllAlbumPreferences() {
        viewModelScope.launch{
            val result = repository.getAllAlbumPreferences()
            result?.let{
                userAlbumPreferences.postValue(it)
            }
        }
    }
    open fun getAllPerformerPreferences() {
        viewModelScope.launch{
            val result = repository.getAllPerformerPreferences()
            result?.let{
                userPerformerPreferences.postValue(it)
            }
        }
    }

    open fun getUserFollowingsGenrePreferences() {
        viewModelScope.launch{
            val result = repository.getUserFollowingsGenrePreferences(username)
            result?.let{
                userGenrePreferences.postValue(it)
            }
        }
    }
    open fun getUserFollowingsAlbumPreferences() {
        viewModelScope.launch{
            val result = repository.getUserFollowingsAlbumPreferences(username)
            result?.let{
                userAlbumPreferences.postValue(it)
            }
        }
    }
    open fun getUserFollowingsPerformerPreferences() {
        viewModelScope.launch{
            val result = repository.getUserFollowingsPerformerPreferences(username)
            result?.let{
                userPerformerPreferences.postValue(it)
            }
        }
    }
    open fun getUserSongRatings() {
        viewModelScope.launch{
            val result = repository.getUserSongRatings(username)
            result?.let{
                userSongRatings.postValue(it)
            }
        }
    }

    open fun getUserAlbumRatings() {
        viewModelScope.launch{
            val result = repository.getUserAlbumRatings(username)
            result?.let{
                userAlbumRatings.postValue(it)
            }
        }
    }
    open fun getUserPerformerRatings() {
        viewModelScope.launch{
            val result = repository.getUserPerformerRatings(username)
            result?.let{
                userPerformerRatings.postValue(it)
            }
        }
    }
    open fun postTracks(tracks: List<Music>) {
        viewModelScope.launch {
            val result = repository.postTracks(username, tracks)
            result?.let{

                batchResult.postValue(true)
            }
        }
    }
    fun processFileAndPostTracks(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            val fileContent = repository.readContentFromUri(contentResolver, uri)
            fileContent?.let{
                val gson = Gson()
                val musicBatch = gson.fromJson(fileContent, MusicBatch::class.java)
                val musics = musicBatch.songs

                musics?.let {
                    parsedMusics.postValue(it)
                    postTracks(it)
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
        val musics = fileContent.lines().filter { it.isNotBlank() }.map { line ->
            // Split each line and create Music objects
            // Assuming the format is exactly as the file content you provided
            // You need to modify the parsing logic based on the actual file format
            val parts = line.split(",")
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
        musics?.let{
            parsedMusics.postValue(it)
            postTracks(it)
        }
    }


}

open class MusicRepository(private val addSongsBatchService: AddSongsBatchService, private val userGenrePreferencesService: UserGenrePreferencesService, private val userAlbumPreferencesService: UserAlbumPreferencesService,
                           private val userPerformerPreferencesService: UserPerformerPreferencesService, private val userGetSongRatingsService: UserGetSongRatingsService, private val userGetAlbumRatingsService: UserGetAlbumRatingsService,
                           private val userGetPerformerRatingsService: UserGetPerformerRatingsService, private val groupGenrePreferencesService: GroupGenrePreferencesService, private val groupAlbumPreferencesService: GroupAlbumPreferencesService,
                           private val groupPerformerPreferencesService: GroupPerformerPreferencesService, private val userFollowingsGenrePreferencesService: UserFollowingsGenrePreferencesService,
                           private val userFollowingsAlbumPreferencesService: UserFollowingsAlbumPreferencesService, private val userFollowingsPerformerPreferencesService: UserFollowingsPerformerPreferencesService,
                           private val allGenrePreferencesService: AllGenrePreferencesService, private val allAlbumPreferencesService: AllAlbumPreferencesService, private val allPerformerPreferencesService: AllPerformerPreferencesService,
                           private val recommendationsService: RecommendationsService
): IMusicRepository {

    override suspend fun getRecommendationsFromDB(token: String, criteriaString: String, targetAudience: String): RecommendationsResponse? {
        return try {
            val gson = Gson()
            val jsonObject = JsonObject()
            // Transform the targetAudience into the required format
            val transformedTargetAudience = when (targetAudience) {
                "FollowingsPrefs" -> "followings"
                "GroupPrefs" -> "group"
                "AllPrefs" -> "all"
                else -> targetAudience
            }
            jsonObject.addProperty("target_audience", transformedTargetAudience)

            // Split the criteriaString into a list and add it to the JSON object
            val criteriaList = criteriaString.split(",").map { criteria ->
                when (criteria.trim()) {
                    "getGenrePrefs" -> "genre"
                    "getAlbumPrefs" -> "album"
                    "getPerformerPrefs" -> "performer"
                    else -> criteria.trim()
                }
            }
            jsonObject.add("criteria_list", gson.toJsonTree(criteriaList))

            // Add the targetAudience to the JSON object
            Log.d("MainHost", "1")
            Log.d("MainHost", jsonObject.toString())

            val response = recommendationsService.getRecommendation(username = token,jsonObject)
            //Log.d("MainHost", response.body().)

            if(response.isSuccessful) {
                //Log.d("MainHost", response.body().toString())
                response.body()
            } else {
                Log.d("MainHost", "1")

                null
            }
        } catch (e: Exception) {
            Log.d("MainHost", e.message.toString())
            null
        }
    }
    override suspend fun getAllGenrePreferences(): UserGenrePreferencesResponse? {
        return try {
            val response = allGenrePreferencesService.getAllGenrePreferences()
            if(response.isSuccessful) {
                response.body()
            }
            else{
                null
            }
        } catch( e: Exception) {
            null
        }
    }
    override suspend fun getAllAlbumPreferences(): UserAlbumPreferencesResponse? {
        return try {
            val response = allAlbumPreferencesService.getAllAlbumPreferences()
            if(response.isSuccessful) {
                response.body()
            }
            else{
                null
            }
        } catch( e: Exception) {
            null
        }
    }

    override suspend fun getAllPerformerPreferences(): UserPerformerPreferencesResponse? {
        return try {
            val response = allPerformerPreferencesService.getAllPerformerPreferences()
            if(response.isSuccessful) {
                response.body()
            }
            else{
                null
            }
        } catch( e: Exception) {
            null
        }
    }


    override suspend fun  getUserFollowingsPerformerPreferences(token: String): UserPerformerPreferencesResponse? {
        return try {
            val response = userFollowingsPerformerPreferencesService.getUserFollowingsPerformerPreferences(token)
            if(response.isSuccessful) {
                response.body()
            }
            else{
                null
            }
        } catch( e: Exception) {
            null
        }
    }
    override suspend fun getUserFollowingsAlbumPreferences(token: String): UserAlbumPreferencesResponse? {
        return try {
            Log.d("MainHost", "1")

            val response = userFollowingsAlbumPreferencesService.getUserFollowingsAlbumPreferences(token)
            Log.d("MainHost", "2")

            if(response.isSuccessful) {
                Log.d("MainHost", "3")

                response.body()
            }
            else{
                Log.d("MainHost", "4")
                null
            }
        } catch( e: Exception) {
            Log.d("MainHost", e.message.toString())

            null
        }
    }
    override suspend fun  getUserFollowingsGenrePreferences(token: String): UserGenrePreferencesResponse? {
        return try {
            //Log.d("MainHost", "1")
            val response = userFollowingsGenrePreferencesService.getUserFollowingsGenrePreferences(token)
            //Log.d("MainHost", "aa " + response.body().toString())
            if(response.isSuccessful) {
                //Log.d("MainHost", "a" + response.body().toString())
                response.body()
            }
            else{
                //Log.d("MainHost", "4")
                null
            }
        } catch( e: Exception) {
            //Log.d("MainHost", e.message.toString())
            null
        }
    }
    override suspend fun getGroupGenrePreferences(token: String): UserGenrePreferencesResponse? {
        return try {
            Log.d("MainHost", "1")
            val response = groupGenrePreferencesService.getGroupGenrePreferences(token)
            Log.d("MainHost", "aa " + response.body().toString())
            if(response.isSuccessful) {
                Log.d("MainHost", "a" + response.body().toString())
                response.body()
            }
            else{
                Log.d("MainHost", "4")
                null
            }
        } catch( e: Exception) {
            Log.d("MainHost", e.message.toString())
            null
        }
    }

    override suspend fun getGroupAlbumPreferences(token: String): UserAlbumPreferencesResponse? {
        return try {
            val response = groupAlbumPreferencesService.getGroupAlbumPreferences(token)
            if(response.isSuccessful) {
                response.body()
            }
            else{
                null
            }
        } catch( e: Exception) {
            null
        }
    }

    override suspend fun getGroupPerformerPreferences(token: String) : UserPerformerPreferencesResponse? {
        return try {
            val response = groupPerformerPreferencesService.getGroupPerformerPreferences(token)
            if(response.isSuccessful){
                response.body()
            }
            else{
                null
            }
        } catch(e: Exception) {
            null
        }
    }
    override suspend fun getUserGenrePreferences(token: String) : UserGenrePreferencesResponse? {
        return try {
            val response = userGenrePreferencesService.getUserGenrePreferences(token)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }
        }catch (e: Exception) {

            null
        }
    }

    override suspend fun getUserAlbumPreferences(token: String) : UserAlbumPreferencesResponse? {
        return try{
            val response = userAlbumPreferencesService.getUserAlbumPreferences(token)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }
        } catch(e: Exception) {
            null
        }
    }

    override suspend fun getUserPerformerPreferences(token: String) : UserPerformerPreferencesResponse? {
        return try {
            val response = userPerformerPreferencesService.getUserPerformerPreferences(token)
            if(response.isSuccessful) {
                response.body()
            }
            else {
                null
            }

        } catch(e: Exception) {
            null
        }
    }

    override suspend fun getUserSongRatings(token: String) : UserGetSongRatingsResponse? {
        return try {
            val gson = Gson()
            val jsonObject = JsonObject()
            jsonObject.addProperty("username", token)
            val response = userGetSongRatingsService.getUserSongRatings(jsonObject)
            if(response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun getUserAlbumRatings(token: String) : UserGetAlbumRatingsResponse? {
        return try {
            val gson = Gson()
            val jsonObject = JsonObject()
            jsonObject.addProperty("username", token)
            val response = userGetAlbumRatingsService.getUserAlbumRatings(jsonObject)
            if(response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun getUserPerformerRatings(token: String) : UserGetPerformerRatingsResponse? {
        return try {
            val gson = Gson()
            val jsonObject = JsonObject()
            jsonObject.addProperty("username", token)
            val response = userGetPerformerRatingsService.getUserPerformerRatings(jsonObject)
            if(response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    override suspend fun postTracks(token: String, tracks: List<Music>) : AddSongsBatchResponse?{
        return try {

            val requestObject = AddSongsBatchRequest(token, tracks)
            val gson = Gson()
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

    override suspend fun readContentFromUri(contentResolver: ContentResolver, uri: Uri): String {
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

class MockMusicRepository : IMusicRepository {

    override suspend fun getRecommendationsFromDB(token: String, criteriaString: String, targetAudience: String): RecommendationsResponse? {
        return null
    }

    override suspend fun getAllGenrePreferences(): UserGenrePreferencesResponse? {
        return null
    }

    override suspend fun getAllAlbumPreferences(): UserAlbumPreferencesResponse? {
        return null
    }

    override suspend fun getAllPerformerPreferences(): UserPerformerPreferencesResponse? {
        return null
    }

    override suspend fun getUserFollowingsPerformerPreferences(token: String): UserPerformerPreferencesResponse? {
        return null
    }

    override suspend fun getUserFollowingsAlbumPreferences(token: String): UserAlbumPreferencesResponse? {
        return null
    }

    override suspend fun getUserFollowingsGenrePreferences(token: String): UserGenrePreferencesResponse? {
        return null
    }

    override suspend fun getGroupGenrePreferences(token: String): UserGenrePreferencesResponse? {
        return null
    }

    override suspend fun getGroupAlbumPreferences(token: String): UserAlbumPreferencesResponse? {
        return null
    }

    override suspend fun getGroupPerformerPreferences(token: String): UserPerformerPreferencesResponse? {
        return null
    }

    override suspend fun getUserGenrePreferences(token: String): UserGenrePreferencesResponse? {
        return null
    }

    override suspend fun getUserAlbumPreferences(token: String): UserAlbumPreferencesResponse? {
        return null
    }

    override suspend fun getUserPerformerPreferences(token: String): UserPerformerPreferencesResponse? {
        return null
    }

    override suspend fun getUserSongRatings(token: String): UserGetSongRatingsResponse? {
        return null
    }

    override suspend fun getUserAlbumRatings(token: String): UserGetAlbumRatingsResponse? {
        return null
    }

    override suspend fun getUserPerformerRatings(token: String): UserGetPerformerRatingsResponse? {
        return null
    }

    override suspend fun postTracks(token: String, tracks: List<Music>): AddSongsBatchResponse? {
        return null
    }

    override suspend fun readContentFromUri(contentResolver: ContentResolver, uri: Uri): String {
        return "0"
    }
}


interface IMusicRepository {
    suspend fun getRecommendationsFromDB(token: String, criteriaString: String, targetAudience: String): RecommendationsResponse?
    suspend fun getAllGenrePreferences(): UserGenrePreferencesResponse?
    suspend fun getAllAlbumPreferences(): UserAlbumPreferencesResponse?
    suspend fun getAllPerformerPreferences(): UserPerformerPreferencesResponse?
    suspend fun getUserFollowingsPerformerPreferences(token: String): UserPerformerPreferencesResponse?
    suspend fun getUserFollowingsAlbumPreferences(token: String): UserAlbumPreferencesResponse?
    suspend fun getUserFollowingsGenrePreferences(token: String): UserGenrePreferencesResponse?
    suspend fun getGroupGenrePreferences(token: String): UserGenrePreferencesResponse?
    suspend fun getGroupAlbumPreferences(token: String): UserAlbumPreferencesResponse?
    suspend fun getGroupPerformerPreferences(token: String): UserPerformerPreferencesResponse?
    suspend fun getUserGenrePreferences(token: String): UserGenrePreferencesResponse?
    suspend fun getUserAlbumPreferences(token: String): UserAlbumPreferencesResponse?
    suspend fun getUserPerformerPreferences(token: String): UserPerformerPreferencesResponse?
    suspend fun getUserSongRatings(token: String): UserGetSongRatingsResponse?
    suspend fun getUserAlbumRatings(token: String): UserGetAlbumRatingsResponse?
    suspend fun getUserPerformerRatings(token: String): UserGetPerformerRatingsResponse?
    suspend fun postTracks(token: String, tracks: List<Music>): AddSongsBatchResponse?
    suspend fun readContentFromUri(contentResolver: ContentResolver, uri: Uri): String
}
