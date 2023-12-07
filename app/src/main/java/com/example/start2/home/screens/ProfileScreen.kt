package com.example.start2.home

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import retrofit2.converter.gson.GsonConverterFactory
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.core.component.dimension.Padding


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
    var enteredUsername by rememberSaveable { mutableStateOf("") }

    userProfile?.let {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Existing content
            Image(
                painter = painterResource(id = R.drawable.default_profile_image),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp)) // Add space here

            Text("${userProfile.username}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp)) // Add space here

            Text("${userProfile.email}", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp)) // Add space here

            ClickableText(
                text = AnnotatedString("Followers: ${userProfile.followersCount}"),
                onClick = { navController.navigateToLeafScreen(LeafScreen.Followers) },
            )

            Spacer(modifier = Modifier.height(4.dp)) // Add space here

            ClickableText(
                text = AnnotatedString("Following: ${userProfile.followingCount}"),
                onClick = { navController.navigateToLeafScreen(LeafScreen.Followers) },
            )

            // Other content...

            Spacer(modifier = Modifier.height(16.dp))

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
                    // Handle the search logic internally, for example, update the profile based on the entered username
                    // You can call a function in your ViewModel to fetch the profile for the entered username
                    navController.navigateToLeafScreen(LeafScreen.Friend(userProfile.username))
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Search")
            }

            // Mock favorite songs list
            val favoriteSongs = listOf(
                SongInfo("Without Me", "Old But Gold", "Eminem", "3:45"),
                SongInfo("Rap God", "Old But Gold", "Eminem", "4:20"),
                SongInfo("Thrift Shop", "Can't See Me", "MackleMore", "2:55"),
                SongInfo("Waka Waka", "World Cup", "Shakira", "5:10"),
                SongInfo("Danza Kuduro", "Ibıza", "Don Omar", "3:30"),
                SongInfo("Without Me", "Old But Gold", "Eminem", "3:45"),
                SongInfo("Rap God", "Old But Gold", "Eminem", "4:20"),
                SongInfo("Thrift Shop", "Can't See Me", "MackleMore", "2:55"),
                SongInfo("Waka Waka", "World Cup", "Shakira", "5:10"),
                SongInfo("Danza Kuduro", "Ibıza", "Don Omar", "3:30"),
                SongInfo("Without Me", "Old But Gold", "Eminem", "3:45"),
                SongInfo("Rap God", "Old But Gold", "Eminem", "4:20"),
                SongInfo("Thrift Shop", "Can't See Me", "MackleMore", "2:55"),
                SongInfo("Waka Waka", "World Cup", "Shakira", "5:10"),
                SongInfo("Danza Kuduro", "Ibıza", "Don Omar", "3:30"),
                // Add more songs as needed
            )

            FavoriteSongsList(favoriteSongs)



        }
    } ?: run {
        // userProfile is null, handle this case (e.g., show a loading indicator or an error message)
        //Text("User profile is null")
    }
}

data class SongInfo(
    val songName: String,
    val albumName: String,
    val singer: String,
    val duration: String
)


/*
@Composable
fun UserProfileContent(userProfile: ProfileViewModel.UserProfile?, navController: NavController) {
    var enteredUsername by rememberSaveable { mutableStateOf("") }

    userProfile?.let {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Existing content
            Image(
                painter = painterResource(id = R.drawable.default_profile_image),
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


                    // Handle the search logic internally, for example, update the profile based on the entered username
                    // You can call a function in your ViewModel to fetch the profile for the entered username
                     navController.navigateToLeafScreen(LeafScreen.Friend(userProfile.username))

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
        //Text("User profile is null")
    }
}*/

@Composable
fun LoadingContent() {
    Text(text = "Loading...", fontSize = 20.sp, color = Color.Gray)
}

@Composable
fun ErrorContent(errorMessage: String) {
    Text(text = "Error: $errorMessage", fontSize = 20.sp, color = Color.Red)
}





@Composable
fun FavoriteSongsList(songs: List<SongInfo>) {
    LazyColumn {
        items(songs) { song ->
            SongListItem(song = song)
            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun SongListItem(song: SongInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Handle item click if needed
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_profile_image),
                contentDescription = "Album Cover",
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${song.songName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.15.sp
                )

                Text(
                    text = song.albumName,
                    fontSize = 14.sp,
                    letterSpacing = 0.1.sp,
                    color = Color.Gray
                )

                Text(
                    text = song.singer,
                    fontSize = 14.sp,
                    letterSpacing = 0.1.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Duration: ${song.duration}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
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


/*@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // Preview function for the ProfileScreen
    val navController = rememberNavController()
   // ProfileScreen(navController, username = )
}*/

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // Preview function for the ProfileScreen
    val navController = rememberNavController()

    // Create mock data for the user profile
    val mockUserProfile = ProfileViewModel.UserProfile(
        username = "mockUsername",
        email = "mockEmail@example.com",
        followersCount = 100,
        followingCount = 50
    )

    // Use the ProfileScreen composable with the mock data
    ProfileScreen(navController = navController)
    UserProfileContent(userProfile = mockUserProfile, navController = navController)


    // Alternatively, you can also pass the mockUserProfile directly if you want to see the populated profile
    // ProfileScreen(navController = navController, userProfile = mockUserProfile)
}



