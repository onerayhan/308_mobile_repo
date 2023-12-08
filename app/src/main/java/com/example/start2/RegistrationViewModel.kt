package com.example.start2
/*
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
    var birthday: String = _birthday.toString()
    var username: String= _username.toString()
    var password: String = _password.toString()
    var email:  String= _email.toString()

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
            put("birthday", birthday)
            put("email", email)
            //put("public_id",publicId)
        }

        val apiUrl = "http://51.20.128.164/api/register"

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
        val apiUrl = "http://51.20.128.164/spoti_login"

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


 */
import com.example.start2.OnCommitContentListener
import com.example.start2.RegistrationStepsListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputContentInfoCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
    var birthday: String
        get() = _birthday.value ?: ""
        set(value) {
            _birthday.value = value
        }

    var username: String
        get() = _username.value ?: ""
        set(value) {
            _username.value = value
        }

    var password: String
        get() = _password.value ?: ""
        set(value) {
            _password.value = value
        }

    var email: String
        get() = _email.value ?: ""
        set(value) {
            _email.value = value
        }


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
    fun saveSpotifyToken(token: String?) {
        _spotifyToken.value = token
    }

    private val _response = MutableLiveData<String?>()
    private val _spotiResponse = MutableLiveData<String?>()
    val response: MutableLiveData<String?> = _response
    private val _spotifyToken = MutableLiveData<String?>()
    val spotifyToken: LiveData<String?> = _spotifyToken
    private val _combinedData = MediatorLiveData<Pair<String?, String?>>().apply {
        addSource(response) { response ->
            value = response to _spotifyToken.value
        }
        addSource(_spotifyToken) { token ->
            value = response.value to token
        }
    }
    val combinedData: LiveData<Pair<String?, String?>> = _combinedData



    fun sendUserDataToApi() {

        Log.d("RegistrationViewModel", "Username: $username")
        Log.d("RegistrationViewModel", "Email: $email")
        Log.d("RegistrationViewModel", "Password: $password")
        Log.d("RegistrationViewModel", "Password: $birthday")



        val jsonObject = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("birthday", birthday)
            put("email", email)
            //put("public_id",publicId)
        }

        val apiUrl = "http://51.20.128.164/api/register"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder().url(apiUrl).post(requestBody).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    _response.postValue(responseBody)
                    Log.d("RegistrationViewModel", "suscsefull")






                } else {
                    _response.postValue("Request was not successful. HTTP code: ${response.code}")
                    Log.d("RegistrationViewModel", "not successful")
                }
            } catch (e: IOException) {
                _response.postValue("Request failed: ${e.message}")
            }
        }
    }
    fun sendSpotifyIntent() {
        val apiUrl = "http://51.20.128.164/spoti_login"

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
