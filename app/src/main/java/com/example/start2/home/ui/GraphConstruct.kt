package com.example.start2.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.start2.services_and_responses.UserGenrePreferencesResponse
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.formatter.DecimalFormatValueFormatter
import createGenreNameFormatter


// Convert SongPopularityData to FloatEntry
fun convertToFloatEntry(data: List<SongPopularityData>): List<FloatEntry> {
    return data.mapIndexed { index, item ->
        FloatEntry(x = index.toFloat(), y = item.listens.toFloat())
    }
}

// Mock data
private val mockSongPopularityData = listOf(
    SongPopularityData(listens = 331, songName = "Haydi Söyle"),
    SongPopularityData(listens = 150, songName = "Blue Raincoat"),
    SongPopularityData(listens = 100, songName = "Lo")
)

//@Preview(showBackground = true)
@Composable
fun PreviewSongPopularityTrendChart() {
    val mockData = convertToFloatEntry(mockSongPopularityData)
    SongPopularityTrendChart(data = mockData)
}


// Generic Entry Collection Creation
private fun <T> createEntryCollection(data: List<T>, selector: (T) -> Float): List<FloatEntry> =
    data.mapIndexed { index, item ->
        FloatEntry(x = index.toFloat(), y = selector(item))
    }

// Generic Line Chart Creation
@Composable
private fun createLineChart(data: List<FloatEntry>, lineColor: Color) {
    val chartEntryModel = ChartEntryModelProducer(data)

    Chart(
        chart = lineChart(
            lines = listOf(lineSpec(lineThickness = 6.dp, lineColor = lineColor))
        ),
        chartModelProducer = chartEntryModel,
        startAxis = rememberStartAxis(axisLabelComponent(color = Color.Red)),
        bottomAxis = rememberBottomAxis(axisLabelComponent(color = Color.DarkGray))
    )
}


@Composable
fun createColumnChart(
    data: List<FloatEntry>,
    columnColor: Color,
    horizontalAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
) {
    val lineComponents = List(data.size) {
        LineComponent(color = columnColor.toArgb(), thicknessDp = 10f) // Customize as needed
    }

    val columnChart = ColumnChart(
        columns = lineComponents,
        spacingDp = DefaultDimens.COLUMN_OUTSIDE_SPACING,
        innerSpacingDp = DefaultDimens.COLUMN_INSIDE_SPACING,
        mergeMode = ColumnChart.MergeMode.Grouped,
        dataLabel = null, // Optional: Add TextComponent for data labels if needed
        dataLabelVerticalPosition = VerticalPosition.Top,
        dataLabelValueFormatter = DecimalFormatValueFormatter(), // Customize as needed
        dataLabelRotationDegrees = 0.0f
    )

    // Assuming ChartEntryModelProducer is used to create the chart model
    val chartEntryModel = ChartEntryModelProducer(data)

    Chart(
        chart = columnChart,
        chartModelProducer = chartEntryModel,
        startAxis = rememberStartAxis(axisLabelComponent(color = Color.Red)),
        bottomAxis = rememberBottomAxis(
            axisLabelComponent(color = Color.DarkGray),
            valueFormatter = horizontalAxisValueFormatter // Use the custom formatter here
        )
    )
}
data class SongPopularityData(
    val listens: Int,
    val songName: String // Added a new field for the song name
)

data class RatingData(
    val rating: Int,
    val reviewerId: String // Added a new field for the reviewer's identifier
)

data class GenreData(
    val popularityMetric: Int,
    val genreName: String // Added a new field for the genre name
)

data class ArtistData(
    val releaseCount: Int,
    val artistName: String // Added a new field for the artist name
)

data class SongData(
    val popularityMetric: Int,
    val songId: String // Added a new field for the song's identifier
)


// Specific Chart Implementations
@Composable
fun SongPopularityTrendChart(data: List<FloatEntry>) {
    createLineChart(data, Color.Black)
}

@Composable
fun RatingDistributionChart(data: List<FloatEntry>) {
    //createColumnChart(data, Color.Black)
}


@Composable
fun ArtistProductivityChart(data: List<FloatEntry>) {
    createLineChart(data, Color.Black)
}

@Composable
fun SongCharacteristicsChart(data: List<FloatEntry>) {
    createLineChart(data, Color.Red)
}
