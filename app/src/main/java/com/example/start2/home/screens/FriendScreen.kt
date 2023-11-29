package com.example.start2.home.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.start2.Friendwiewmodel
import com.example.start2.R

@Composable
fun FriendScreen(
    friendUsername: String,  // Parameter for friend's username
   friendViewModel: Friendwiewmodel  // Pass the ProfileViewModel
) {
    // Use the friend's username to fetch their profile
    //tryut
    friendViewModel.fetchUserProfile(friendUsername)
    // Observing the state from the ViewModel
    val friendProfile by   friendViewModel.userProfile
    val isLoading by   friendViewModel.loading
    val error by   friendViewModel.error

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FriendProfileContent(friendProfile,friendViewModel )

        // Add follow/unfollow button based on logic (for simplicity, adding a clickable text)
        ClickableText(
            text = AnnotatedString("Follow/Unfollow"),
            onClick = {
                // Implement your logic for follow/unfollow here
                // Example: profileViewModel.toggleFollowStatus(friendUsername)
            },
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
@Composable
fun FriendProfileContent(friendProfile: Friendwiewmodel.UserProfile?, profileViewModel: Friendwiewmodel) {
    friendProfile?.let {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Image",
                modifier = Modifier.size(100.dp)
            )
            Text("Username: ${friendProfile.username}")
            Text("Email: ${friendProfile.email}")
            Text(
                text = AnnotatedString("Followers: ${friendProfile.followersCount}"),

            )
            Text(text = "Following: ${friendProfile.followingCount}", fontSize = 18.sp)
            // Add additional profile details here as needed
        }
    } ?: run {
        // friendProfile is null, handle this case (e.g., show a loading indicator or an error message)
        Text("Friend profile is null")
    }
}


