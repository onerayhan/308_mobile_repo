package com.example.start2
import com.example.start2.OnCommitContentListener
import com.example.start2.RegistrationStepsListener
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputContentInfoCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.UUID

class RegistrationViewModel : ViewModel() {



    // Private mutable data which we will update
    private val _email = MutableLiveData<String>()
    private val _birthday = MutableLiveData<String>()
    private val _username = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    // Public immutable data which the UI can observe
    val birthday: LiveData<String> = _birthday
    val username: LiveData<String> = _username
    val password: LiveData<String> = _password

    fun saveEmail(email: String) {
        _email.value = email
    }

    fun saveBirthday(birthday: String) {
        _birthday.value = birthday
    }

    fun saveUsername(username: String) {
        _username.value = username
    }

    fun savePassword(password: String) {
        _password.value = password
    }

    private val _response = MutableLiveData<String?>()
    private val _spotiResponse = MutableLiveData<String?>()
    val response: MutableLiveData<String?> = _response

    fun sendUserDataToApi() {
        val publicId = UUID.randomUUID().toString().replace("-", "")



        val jsonObject = JSONObject().apply {
            put("username", username)
            put("password", password)
            //put("public_id",publicId)
        }

        val apiUrl = "http://13.51.167.155/api/register"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder().url(apiUrl).post(requestBody).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    _response.postValue(responseBody)
                    





                } else {
                    _response.postValue("Request was not successful. HTTP code: ${response.code}")
                }
            } catch (e: IOException) {
                _response.postValue("Request failed: ${e.message}")
            }
        }
    }
    fun sendSpotifyIntent() {
        val apiUrl = "http://13.51.167.155/spoti_login"

        val jsonObject = JSONObject().apply {
            put("username", username)
            put("password", password)
            //put("public_id",publicId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder().url(apiUrl).post(requestBody).build()
                val response = client.newCall(request).execute()
                if(response.isSuccessful) {
                    val responseBody = response.body?.string()
                    _spotiResponse.postValue(responseBody)
                }
                else{
                    _spotiResponse.postValue("Request was not successful. HTTP code: ${response.code}")
                }


            }
            catch (e: IOException) {
                _spotiResponse.postValue("Request failed: ${e.message}")
            }
        }
    }
/*
    fun sendSpotifyToken(string token) {
        val apiUrl = ""
    }

*/

}
