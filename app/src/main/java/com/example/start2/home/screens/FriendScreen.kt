package com.example.start2.home.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    friendViewModel.fetchUserProfile(friendUsername)
    // Observing the state from the ViewModel
    val friendProfile by friendViewModel.userProfile
    val isLoading by friendViewModel.loading
    val error by friendViewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Friend's profile card
        if (friendProfile != null) {
            FriendProfileCard(friendProfile)
        } else {
            Text("Loading...", fontWeight = FontWeight.Bold)
        }

        // Add follow/unfollow button based on logic (for simplicity, adding a clickable text)
        FollowUnfollowButton(
            onClick = {
                // Implement your logic for follow/unfollow here
                // Example: profileViewModel.toggleFollowStatus(friendUsername)
            }
        )
    }
}

@Composable
fun FriendProfileCard(friendProfile: Friendwiewmodel.UserProfile?) {
    friendProfile?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(MaterialTheme.shapes.medium)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.default_profile_image),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("${friendProfile.username}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Email: ${friendProfile.email}")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Followers: ${friendProfile.followersCount}")
                    Text("Following: ${friendProfile.followingCount}")
                }
                // Add additional profile details here as needed
            }
        }
    }
}

@Composable
fun FollowUnfollowButton(onClick: () -> Unit) {
    var isFollowing by remember { mutableStateOf(false) }

    ClickableText(
        text = AnnotatedString(
            if (isFollowing) "Unfollow" else "Follow",
            spanStyle = androidx.compose.ui.text.SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        ),
        onClick = {
            isFollowing = !isFollowing
            onClick()
        },
        modifier = Modifier.padding(top = 16.dp)

    )
}

@Composable
@Preview
fun FriendScreenPreview() {
    val friendProfile = Friendwiewmodel.UserProfile(
        username = "JohnDoe",
        email = "john.doe@example.com",
        followersCount = 100,
        followingCount = 50
    )

    FriendScreen(
        friendUsername = "JohnDoe",  // Provide a mock username
        friendViewModel = remember {
            object : Friendwiewmodel() {
                init {
                    _userProfile.value = friendProfile
                }
            }
        }
    )
}






/*@Composable
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
}*/