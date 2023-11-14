import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import coil.compose.rememberImagePainter

// https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Google_Images_2015_logo.svg/1200px-Google_Images_2015_logo.svg.png


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
        Image(painter = rememberImagePainter(follower.profilePic), contentDescription = null)
        Text(text = follower.username)
    }
}

