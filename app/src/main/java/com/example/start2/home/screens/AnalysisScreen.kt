package com.example.start2.home.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.start2.home.AnalysisViewModel
import com.example.start2.home.ui.ArtistProductivityChart
import com.example.start2.home.ui.GenrePopularityChart
import com.example.start2.home.ui.RatingDistributionChart
import com.example.start2.home.ui.SongCharacteristicsChart
import com.example.start2.home.ui.SongPopularityTrendChart



import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.start2.home.ui.ArtistData
import com.example.start2.home.ui.GenreData
import com.example.start2.home.ui.RatingData
import com.example.start2.home.ui.SongData
import com.example.start2.home.ui.SongPopularityData
import com.patrykandpatrick.vico.core.entry.FloatEntry

@Preview(showBackground = true)
@Composable
fun AnalysisScreenPreview() {
    val dummyNavController = rememberNavController()
    AnalysisScreen(navController = dummyNavController)
}
enum class AnalysisOption(val displayName: String) {
    SongPopularity("Song Popularity Over Time"),
    RatingDistribution("Rating Distribution"),
    PopularGenres("Most Popular Genres"),
    ArtistProductivity("Artist Productivity Over Years"),
    SongCharacteristics("Song Characteristics and Popularity"),
}

@Composable
fun AnalysisScreen(navController: NavController) {
    // Initialize the ViewModel using viewModel()
    val viewModel: AnalysisViewModel = viewModel()

    var selectedOption by remember { mutableStateOf(AnalysisOption.SongPopularity) }

    Scaffold(
        topBar = { AnalysisTopBar() }
    ) { contentPadding ->
        // Use contentPadding for the Column
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            AnalysisOptionSelector(selectedOption) { option ->
                selectedOption = option
                viewModel.fetchDataForOption(option)
            }
            AnalysisChartArea(viewModel, selectedOption)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisTopBar() {
    TopAppBar(title = { Text("Data Analysis") })
}
@Composable
fun AnalysisOptionSelector(selectedOption: AnalysisOption, onOptionSelected: (AnalysisOption) -> Unit) {
    val expanded = remember { mutableStateOf(false) } // State to track if the dropdown is expanded

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Select an Analysis Option", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown menu to select an option
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            AnalysisOption.values().forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        onOptionSelected(option)
                        expanded.value = false
                    }
                )
            }
        }

        // Button to show/hide the DropdownMenu
        Button(onClick = { expanded.value = true }) {
            Text("Show Options")
        } }
        }

@Composable
fun AnalysisChartArea(viewModel: AnalysisViewModel, selectedOption: AnalysisOption) {
    val chartData by viewModel.chartData.observeAsState(listOf())

    // Each chart function directly accepts List<FloatEntry>
    when (selectedOption) {
        AnalysisOption.SongPopularity -> SongPopularityTrendChart(chartData)
        AnalysisOption.RatingDistribution -> RatingDistributionChart(chartData)
        AnalysisOption.PopularGenres -> GenrePopularityChart(chartData)
        AnalysisOption.ArtistProductivity -> ArtistProductivityChart(chartData)
        AnalysisOption.SongCharacteristics -> SongCharacteristicsChart(chartData)
    }
}