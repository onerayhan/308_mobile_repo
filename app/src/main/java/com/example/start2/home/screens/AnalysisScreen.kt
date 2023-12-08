import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.start2.home.AnalysisViewModel
import com.example.start2.home.navigators.LeafScreen
import com.example.start2.home.spotify.SpotifyViewModel
import com.example.start2.home.ui.ArtistProductivityChart
import com.example.start2.home.ui.GenrePopularityChart
import com.example.start2.home.ui.RatingDistributionChart
import com.example.start2.home.ui.SongCharacteristicsChart
import com.example.start2.home.ui.SongPopularityTrendChart

enum class AnalysisOption(val displayName: String) {
    SongPopularity("Song Popularity Over Time"),
    RatingDistribution("Rating Distribution"),
    PopularGenres("Most Popular Genres"),
    ArtistProductivity("Artist Productivity Over Years"),
    SongCharacteristics("Song Characteristics and Popularity"),
}

@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    val dummyNavController = rememberNavController()
    AnalysisScreen(navController = dummyNavController, viewModel = null)
}
private fun NavController.navigateToLeafScreen(leafScreen: LeafScreen) {
    navigate(leafScreen.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}

@Composable
fun AnalysisScreen(navController: NavController, viewModel: SpotifyViewModel?) {
    val chartList = listOf(
        AnalysisOption.SongPopularity,
        AnalysisOption.RatingDistribution,
        AnalysisOption.PopularGenres,
        AnalysisOption.ArtistProductivity,
        AnalysisOption.SongCharacteristics
    )
    val analysisViewModel: AnalysisViewModel = viewModel()
    var selectedOption by remember { mutableStateOf(AnalysisOption.SongPopularity) }

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

            // Use LazyRow for horizontal scrolling
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(Color.White),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(chartList) { chart ->
                    ChartSelector(chart, selectedOption) {
                        selectedOption = it
                        analysisViewModel.fetchDataForOption(it)
                    }
                }
            }

            // LazyColumn for the main content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AnalysisChartArea(analysisViewModel, selectedOption)
                }
            }
        }
    }
}
@Composable
fun ChartSelector(chart: AnalysisOption, selectedOption: AnalysisOption, onClick: (AnalysisOption) -> Unit) {
    val textColor = if (chart == selectedOption) MaterialTheme.colorScheme.primary else Color.Gray
    Row(
        modifier = Modifier
            .padding(end = 16.dp)
            .clickable { onClick(chart) }
    ) {
        Text(
            text = chart.displayName,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisTopBar(title: String) {
    SmallTopAppBar(title = {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.End
            )
        )
    })
}

@Composable
fun AnalysisChartArea(viewModel: AnalysisViewModel, selectedOption: AnalysisOption) {
    val chartData by viewModel.chartData.observeAsState(listOf())
    when (selectedOption) {
        AnalysisOption.SongPopularity -> SongPopularityTrendChart(chartData)
        AnalysisOption.RatingDistribution -> RatingDistributionChart(chartData)
        AnalysisOption.PopularGenres -> GenrePopularityChart(chartData)
        AnalysisOption.ArtistProductivity -> ArtistProductivityChart(chartData)
        AnalysisOption.SongCharacteristics -> SongCharacteristicsChart(chartData)
    }
}
