package com.example.start2.home.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

import com.example.start2.ProfileViewModel
import com.example.start2.ProfileViewModelFactory
import com.example.start2.R
import com.example.start2.UserPreferences
import com.example.start2.home.UserProfileContent
import com.example.start2.home.navigators.LeafScreen
import java.io.File

@Composable
fun FriendScreen(

) {

    val context = LocalContext.current
    val userPreferences= remember{UserPreferences(context)}
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val username by profileViewModel.username.observeAsState()



    Log.d("ProfileScreen", "ProfileScreen123342: ${(profileViewModel.username)}")
    Log.d("ProfileScreen", "ProfileScreen123342: ${(username)}")

    profileViewModel.fetchUserProfileByUsername("123")

    // Save the username in the ViewModel

    // Observing the state from the ViewModel
    val userProfile by profileViewModel.userProfile
    val isLoading by profileViewModel.loading
    val error by profileViewModel.error

    UserProfileContent(userProfile)

}
@Composable
fun UserProfileContent(userProfile: ProfileViewModel.UserProfile?) {
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

            androidx.compose.material3.Text("Username: ${userProfile.username}")
            androidx.compose.material3.Text("Email: ${userProfile.email}")
            androidx.compose.material3.Text(
                text = AnnotatedString("Followers: ${userProfile.follower_count}"),

            )
            androidx.compose.material3.Text(
                text = "Following: ${userProfile.followed_count}",
                fontSize = 18.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        // Handle follow button click
                       profileViewModel.followUser("123")

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Follow")
                }

                Button(
                    onClick = {
                        // Handle unfollow button click
                       profileViewModel.unfollowUser("123")

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text("Unfollow")
                }
            }

        }
    } ?: run {
        // userProfile is null, handle this case (e.g., show a loading indicator or an error message)
        androidx.compose.material3.Text("User profile is null")
    }
}
