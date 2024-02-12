package com.example.start2.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.start2.home.Profile.ProfileViewModel
import com.example.start2.home.Profile.ProfileViewModelFactory
import com.example.start2.home.Profile.UserPreferences
import com.example.start2.home.navigators.LeafScreen
import coil.compose.rememberImagePainter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.start2.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.start2.viewmodels.DataHolder
import com.example.start2.viewmodels.MyData
import java.io.File



@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val username by profileViewModel.username.observeAsState()





    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }


    // Observing the state from the ViewModel
    val userProfile by profileViewModel.userProfile
    val isLoading by profileViewModel.loading
    val error by profileViewModel.error



    LazyColumn(
        modifier = Modifier.fillMaxSize(), // Ekranın tamamını kapla
        contentPadding = PaddingValues(16.dp) // İçeriği kenarlardan 16dp içeri al
    ) {
        item {
            UserProfileContent(userProfile, navController)
        }
    }
}




@Composable
fun UserProfileContent(userProfile: ProfileViewModel.UserProfile?, navController: NavController) {
    val context = LocalContext.current
    val userPreferences= remember { UserPreferences(context) }
    val profileViewModel = viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val songListLiveData = profileViewModel.songList
    val genrePref by profileViewModel.genrePreferences.observeAsState()

    val favoriteSongs = songListLiveData.observeAsState(initial = emptyList())


    val selectedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }

    var enteredUsername by rememberSaveable { mutableStateOf("") }

    var selectedImageFile by rememberSaveable { mutableStateOf<File?>(null) }


    profileViewModel.getUserGenrePreferences()




    val openGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the result of the gallery picker here
        uri?.let {
            selectedImageUri.value = it
        }
    }

    profileViewModel.fetchUserSongs()


    userProfile?.let { profile ->

        Box(
            modifier = Modifier
                .requiredWidth(width = 400.dp)
                .requiredHeight(height = 800.dp)
                .padding(top=50.dp)
                .background(color = Color.White)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(400.dp)
                    .requiredHeight(265.dp)
                    .background(color = Color(7, 26, 46))
            ) {
                Image(
                    painter = rememberImagePainter(data = selectedImageUri.toString()),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 205.dp, y = 50.dp)
                        .requiredSize(120.dp)
                        .clip(CircleShape)
                        .background(color= Color.White)
                )
            }

            Text(
                text = profile.username,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = (-81).dp, y = 88.dp)
            )

            // Display Group Name
            Text(
                text = "Group: ",
                color = Color.White,
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 48.dp, y = 320.dp)
            )

            // Display Member Count
            Text(
                text = "Members: ",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 48.dp, y = 350.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.rectangle_9),
                contentDescription = "Rectangle 14",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 188.dp,
                        y = 193.dp
                    )
                    .requiredWidth(width = 143.dp)
                    .requiredHeight(height = 44.dp)
                    .clip(shape = RoundedCornerShape(15.dp)))
            Text(
                text = "Group",
                color = Color(0xFFF1F5F1),
                style = TextStyle(
                    fontSize = 18.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 201.dp,
                        y = 202.dp
                    )
                    .requiredWidth(width = 128.dp)
                    .requiredHeight(height = 26.dp))
            Box(
                modifier = Modifier
                    .requiredWidth(width = 300.dp)
                    .requiredHeight(height = 250.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .clickable {
                        // Galeriyi aç
                        navController.navigateToLeafScreen(LeafScreen.UserGroupScreen)

                    }
            ) {
                if (selectedImageUri.value != null) {
                    Image(
                        painter = rememberImagePainter(data = selectedImageUri.value.toString()),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 205.dp, y = 50.dp)
                            .requiredSize(120.dp)
                            .clip(CircleShape)
                            .background(color = Color.White)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.rectangle_9),
                        contentDescription = "Rectangle 16",
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 27.5.dp,
                                y = 144.5.dp
                            )
                            .requiredWidth(width = 140.dp)
                            .requiredHeight(height = 140.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                    )
                }

                Text(
                    text = "Edit profile",
                    color = Color(0xFFF3F3F3),
                    style = TextStyle(
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 44.dp,
                            y = 204.dp
                        )
                )
            }


            Image(
                painter = painterResource(id = R.drawable.rectangle_17),
                contentDescription = "Rectangle 17",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = (-3).dp, y = 255.dp)
                    .requiredWidth(width = 400.dp)
                    .requiredHeight(height = 545.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.rectangle_16),
                contentDescription = "Rectangle 16",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 198.dp,
                        y = 282.dp
                    )
                    .requiredWidth(width = 124.dp)
                    .requiredHeight(height = 37.dp)
                    .clip(shape = RoundedCornerShape(13.dp)))
            Text(
                text = "Followings:${profile.followed_count} ",
                color = Color.White,
                style = TextStyle(
                    fontSize = 16.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 208.dp,
                        y = 292.dp
                    ))

            Image(
                painter = painterResource(id = R.drawable.rectangle_9),
                contentDescription = "Rectangle 9",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 37.dp, y = 282.dp)
                    .requiredWidth(width = 131.dp)
                    .requiredHeight(height = 37.dp)
                    .clip(shape = RoundedCornerShape(13.dp))
            )

            Text(
                text = "Followers:${profile.follower_count} ",
                color = Color.White,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 53.dp, y = 293.dp)
                    .clickable {
                        navController.navigateToLeafScreen123(LeafScreen.Followers)
                    }
            )

            Box(
                modifier = Modifier
                    .padding(start = 40.dp, top = 350.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle_12__1_),
                    contentDescription = "Rectangle 12",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .clip(shape = RoundedCornerShape(16.dp))
                )

                OutlinedTextField(
                    value = enteredUsername,
                    onValueChange = { enteredUsername = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                    placeholder = { Text("Search friend", color = Color.White) },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                )
                val dataToSend = MyData(enteredUsername)

                // Assign the data to the singleton object
                DataHolder.myData = dataToSend

                Button(
                    onClick = {
                        navController.navigateToLeafScreen(LeafScreen.FriendScreen)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    Text("enter")
                }
            }


            Column(
                modifier = Modifier.padding(top=100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredHeight(height = 320.dp) // Adjust height as necessary
                ) {
                    Text(
                        text = "Top tracks",
                        color = Color.White,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.align(Alignment.TopStart)
                            .offset(x = 48.dp, y= 320.dp)

                    )
                    Text(
                        text = "More",
                        color = Color(0xff5fc36a),
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 250.dp, y= 320.dp)
                    )
                }


                LazyColumn(
                    modifier = Modifier
                        .padding(top=30.dp)
                        .fillMaxWidth()
                ) {


                    items(favoriteSongs.value) { song ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(
                                    if (favoriteSongs.value.indexOf(song) % 2 == 0) Color(
                                        0xff1d1d20
                                    ) else Color.Transparent
                                )
                        ) {
                            Text(
                                text = song.title,
                                color = Color.White,
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(8.dp)
                            )
                            Text(
                                text = song.artist,
                                color = Color.White,
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(8.dp)
                            )
                            Text(
                                text = song.album,
                                color = Color.White,
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            )
                        }
                    }
                }




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
            navigate(leafScreen.route){
                launchSingleTop = true
                restoreState = true
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }
    }
}

private fun NavController.navigateToLeafScreen123(leafScreen: LeafScreen) {
    when (leafScreen) {
        is LeafScreen.UserGroupScreen -> {
            // Navigate to the UserGroupScreen
            navigate(leafScreen.route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }
        // Handle other LeafScreen cases if needed
        else -> {
            // Navigate to other screens
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











@Composable
fun ProfileScreenPreview() {
    // Create a NavController for preview
    val navController = rememberNavController()

    // Show the ProfileScreen with mock data
    ProfileScreen(navController)
}

@Preview(showBackground = true, name = "UserProfileContent Preview")
@Composable
fun PreviewUserProfileContent() {
    // Mock data for UserProfile
    val mockUserProfile = ProfileViewModel.UserProfile(
        username = "JohnDoe",
        followed_count = 100,
        follower_count = 150,
        email = "example@email.com"
    )

    // Create a NavController for preview
    val navController = rememberNavController()

    // Show the UserProfileContent with mock data
    UserProfileContent(userProfile = mockUserProfile, navController = navController)
}