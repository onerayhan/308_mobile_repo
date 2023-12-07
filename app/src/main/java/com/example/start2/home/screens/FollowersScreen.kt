

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.size.Scale
import com.example.start2.R

// https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Google_Images_2015_logo.svg/1200px-Google_Images_2015_logo.svg.png

/*
data class Follower(val username: String, val profilePic: String)


@Composable
fun FollowersScreen() {
    val followers = listOf(
        Follower("user1", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Google_Images_2015_logo.svg/1200px-Google_Images_2015_logo.svg.png"),
        Follower("user2", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Google_Images_2015_logo.svg/1200px-Google_Images_2015_logo.svg.png"),
        // Add more dummy followers
    )

    LazyColumn {
        items(followers) { follower ->
            FollowerItem(follower)
        }
    }
}

@Composable
fun FollowerItem(follower: Follower) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = rememberAsyncImagePainter(follower.profilePic), contentDescription = null)
        Text(text = follower.username)
    }
}


@Composable
@Preview
fun FollowersScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        FollowersScreen()
    }
}
*/

data class Follower(val username: String, val profilePic: String)

@Composable
fun FollowersScreen() {
    val followers = listOf(
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        Follower("Serhat Akın", "https://example.com/profile1.jpg"),
        Follower("Serdar Ali", "https://example.com/profile2.jpg"),
        // Add more dummy followers
    )

    LazyColumn {
        items(followers) { follower ->
            FollowerItem(follower)
        }
    }
}

@Composable
fun FollowerItem(follower: Follower) {
    val customBodyTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )

    Row(

        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        // Profile picture
        Image(
            painter = rememberImagePainter(
                data = follower.profilePic,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.default_profile_image) // Add a placeholder image
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Follower information
        Column {
            Text(text = follower.username, style = customBodyTextStyle)
            Text(text = "Profile", style = customBodyTextStyle.copy(fontSize = 14.sp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FollowersScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        FollowersScreen()
    }
}