import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.start2.ProfileViewModel
import com.example.start2.ProfileViewModelFactory
import com.example.start2.UserPreferences
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.ui.createColumnChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry





private fun NavController.navigateToLeafScreen(leafScreen: LeafScreen) {
    navigate(leafScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}


fun convertFollowingGenrePreferencesToChartData(followingGenrePreferences: ProfileViewModel.UserFollowingsGenrePreferencesResponse): List<FloatEntry> {
    // Convert the following genre preferences data to the format required by your chart
    return followingGenrePreferences.genres.mapIndexed { index, genrePreference ->
        FloatEntry(x = index.toFloat(), y = genrePreference.count.toFloat())
    }
}

data class PerformerPreference(val performer: String, val count: Int)
data class UserFollowingGenrePreferencesResponse(val genres: List<ProfileViewModel.GenrePreference>)

fun convertPerformerPreferencesToChartData(performerPreferences: ProfileViewModel.UserPerformerPreferencesResponse): List<FloatEntry> {
    // Convert the performer preferences data to the format required by your chart
    return performerPreferences.performers.mapIndexed { index, performerPreference ->
        FloatEntry(x = index.toFloat(), y = performerPreference.count.toFloat())
    }
}
// Convert GenrePreferences to Chart Data
fun convertGenrePreferencesToChartData(genrePreferences: com.example.start2.ProfileViewModel.UserGenrePreferencesResponse): List<FloatEntry> {
    // Convert the genre preferences data to the format required by your chart
    return genrePreferences.genre.mapIndexed { index, genrePreference ->
        FloatEntry(x = index.toFloat(), y = genrePreference.count.toFloat())
    }
}
// Create a custom AxisValueFormatter for the x-axis
fun createGenreNameFormatter(genrePreferences: ProfileViewModel.UserGenrePreferencesResponse): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    val indexToGenreName = genrePreferences.genre.mapIndexed { index, genrePreference ->
        index.toFloat() to genrePreference.genre
    }.toMap()

    return AxisValueFormatter { value, _ ->
        indexToGenreName[value] ?: ""
    }
}

fun createFollowingGenreNameFormatter(followingGenrePreferences: ProfileViewModel.UserFollowingsGenrePreferencesResponse): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    // Map index (Float) to genre names
    val indexToGenreName = followingGenrePreferences.genres.mapIndexed { index, genrePreference ->
        index.toFloat() to genrePreference.genre
    }.toMap()

    // Return a custom AxisValueFormatter that uses this mapping
    return AxisValueFormatter { value, _ ->
        indexToGenreName[value] ?: ""
    }
}

fun createPerformerNameFormatter(performerPreferences: ProfileViewModel.UserPerformerPreferencesResponse): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    // Map index (Float) to performer names
    val indexToPerformerName = performerPreferences.performers.mapIndexed { index, performerPreference ->
        index.toFloat() to performerPreference.performer
    }.toMap()

    // Return a custom AxisValueFormatter that uses this mapping
    return AxisValueFormatter { value, _ ->
        indexToPerformerName[value] ?: ""
    }
}


@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    val dummyNavController = rememberNavController()
    AnalysisScreen(dummyNavController,null)
}

// Enum class for analysis options
enum class AnalysisOption(val displayName: String) {
    PopularGenres("Most Popular Genres"),
    PopularPerformers("Most Popular Performers"),
    FollowingGenrePreferences("Follower's Most Popular Genres"),
}

enum class ChartOption {
    GenrePreferences,
    PerformerPreferences,
    FollowingGenrePreferences
}


@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: SpotifyViewModel?,
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    // State to keep track of the selected chart option
    var selectedOption by remember { mutableStateOf(AnalysisOption.PopularGenres) }
    var selectedChartOption by remember { mutableStateOf(ChartOption.GenrePreferences) }
    val expanded = remember { mutableStateOf(false) }
    val profileViewModel =
        viewModel<ProfileViewModel>(factory = ProfileViewModelFactory(userPreferences))
    val username by profileViewModel.username.observeAsState()
    //username?.let { profileViewModel.getUserGenrePreferences(it) }
    // username?.let{  profileViewModel.getUserGenrePreferences(it)}
    //username?.let{ profileViewModel.getUserFollowingsGenrePreferences(it)}

    username?.let { profileViewModel.getUserGenrePreferences(it) }
    username?.let { profileViewModel.getUserPerformerPreferences(it) }
    //profileViewModel.getUserFollowingsGenrePreferences(username)

    val genrePreferences by profileViewModel.genrePreferences.observeAsState()
    val performerPreferences by profileViewModel.userperformerPreferences.observeAsState()
    val followingGenrePreferences by profileViewModel.userFollowingsGenrePreferences.observeAsState()
    Scaffold(
        topBar = { AnalysisTopBar(title = "Your Song Activity") }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        // Handle button click here
                        navController?.navigateToLeafScreen(LeafScreen.AnalysisTable)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Go to Analysis Table")
                }
            }
            followingGenrePreferences?.let {
                val horizontalAxisValueFormatter1 = createFollowingGenreNameFormatter(it)
                val chartData = convertFollowingGenrePreferencesToChartData(it)
                // createColumnChart(chartData, Color.Black, horizontalAxisValueFormatter1)

            }
            Spacer(modifier = Modifier.height(50.dp))

            performerPreferences?.let {
                val horizontalAxisValueFormatter1 = createPerformerNameFormatter(it)
                val chartData = convertPerformerPreferencesToChartData(it)
                //  createColumnChart(chartData, Color.Black, horizontalAxisValueFormatter1)

            }
            genrePreferences?.let {
                // Ensure genrePreferences is not null before passing it
                val horizontalAxisValueFormatter = createGenreNameFormatter(it)

                val chartData = convertGenrePreferencesToChartData(it)
                createColumnChart(chartData, Color.Black, horizontalAxisValueFormatter)
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisTopBar(title: String) {
    TopAppBar(title = {
        Text(
            text = title,
            style = MaterialTheme.typography.h2.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.End
            )
        )
    })
}

