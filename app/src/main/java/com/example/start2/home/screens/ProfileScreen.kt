package com.example.start2.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.navigators.RootScreen
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController

import com.example.start2.ProfilePictureViewModel
import com.example.start2.ProfileViewModel
import com.example.start2.R
import com.example.start2.RegistrationViewModel
import com.example.start2.SongViewModel

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.start2.ProfileViewModelFactory
import com.example.start2.UserPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.start2.home.screens.FriendScreen
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


// Data class to represent the structure of the API response


// Retrofit service interface

// ViewModel to handle API calls and store data

// ViewModel factory to provide the ViewModel to the composable





@Composable
fun ProfileScreen(
    navController: NavController) {

    val context = LocalContext.current
    val userPreferences= remember{UserPreferences(context)}
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val username by profileViewModel.username.observeAsState()



    Log.d("ProfileScreen", "ProfileScreen123342: ${(profileViewModel.username)}")
    Log.d("ProfileScreen", "ProfileScreen123342: ${(username)}")

    profileViewModel.fetchUserProfile();



    // Save the username in the ViewModel

    // Observing the state from the ViewModel
    val userProfile by profileViewModel.userProfile
    val isLoading by profileViewModel.loading
    val error by profileViewModel.error

    UserProfileContent(userProfile, navController)

}

@Composable
fun UserProfileContent(userProfile: ProfileViewModel.UserProfile?, navController: NavController) {
    val context = LocalContext.current
    val userPreferences= remember{UserPreferences(context)}
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))


    var enteredUsername by rememberSaveable { mutableStateOf("") }
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var selectedImageFile by rememberSaveable { mutableStateOf<File?>(null) }
    val openGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the result of the gallery picker here
        uri?.let {
            selectedImageUri = it
        }
    }


    userProfile?.let {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Existing content
            Image(
                painter = rememberImagePainter(data = selectedImageUri),
                contentDescription = "Profile Image",
                modifier = Modifier.size(100.dp)
            )

            Text("Username: ${userProfile.username}")
            Text("Email: ${userProfile.email}")
            ClickableText(
                text = AnnotatedString("Followers: ${userProfile.followersCount}"),
                onClick = { navController.navigateToLeafScreen(LeafScreen.Followers) },
            )
            Text(text = "Following: ${userProfile.followingCount}", fontSize = 18.sp)

            // Add additional profile details here as needed

            // TextField for entering the username
            OutlinedTextField(
                value = enteredUsername,
                onValueChange = { enteredUsername = it },
                label = { Text("Enter Username") },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )

            // Button for initiating the search on the same page
            Button(
                onClick = {
                    // Launch the gallery to pick an image
                    openGalleryLauncher.launch("image/*")
                    selectedImageUri?.let { uri ->
                        // Convert URI to File
                        val selectedImageFile = File(uri.path)

                        // Check if the file exists
                        if (selectedImageFile.exists()) {
                            // Call the API to upload the photo
                            profileViewModel.uploadPhoto(selectedImageFile)
                            Log.d("ProfileScreen", "ftoyu attık: ${(profileViewModel.username)}")



                        } else {
                            // Handle the case where the file does not exist
                            // (e.g., show an error message)
                            Log.d("ProfileScreen", "ftoyu atamadık: ${(profileViewModel.username)}")
                        }
                    }


                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Select Profile Image")
            }


            Button(
                onClick = {



                   navController.navigateToLeafScreen(LeafScreen.FriendScreen)


                    // Handle the search logic internally, for example, update the profile based on the entered username
                    // You can call a function in your ViewModel to fetch the profile for the entered username





                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Search")
            }
        }
    } ?: run {
        // userProfile is null, handle this case (e.g., show a loading indicator or an error message)
        Text("User profile is null")
    }
}

@Composable
fun LoadingContent() {
    Text(text = "Loading...", fontSize = 20.sp, color = Color.Gray)
}

@Composable
fun ErrorContent(errorMessage: String) {
    Text(text = "Error: $errorMessage", fontSize = 20.sp, color = Color.Red)
}



@Composable
fun FavoriteSongsList(songs: List<String>) {
    Column {
        songs.forEach { song ->
            Text(text = song)
        }
    }
}
private fun NavController.navigateToLeafScreen(leafScreen: LeafScreen) {
    when (leafScreen) {
        is LeafScreen.Friend -> {
            navigate(leafScreen.route.replace("{username}", leafScreen.username)) {
                launchSingleTop = true
                restoreState = true
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }
        else -> {
            navigate(leafScreen.route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // Preview function for the ProfileScreen
    val navController = rememberNavController()
   // ProfileScreen(navController, username = )
}
