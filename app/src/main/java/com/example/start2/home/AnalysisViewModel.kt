package com.example.start2.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.start2.home.screens.AnalysisOption
import com.example.start2.home.ui.ArtistData
import com.example.start2.home.ui.GenreData
import com.example.start2.home.ui.RatingData
import com.example.start2.home.ui.SongData
import com.example.start2.home.ui.SongPopularityData
import com.patrykandpatrick.vico.core.entry.FloatEntry

class AnalysisViewModel : ViewModel() {
    // LiveData to hold the chart data
    val chartData = MutableLiveData<List<FloatEntry>>()

    // Mock data
    private val mockSongPopularityData = listOf(
        SongPopularityData(listens = 331, songName = "Haydi Söyle"),
        SongPopularityData(listens = 130, songName = "Blue Raincoat"),
        SongPopularityData(listens = 100, songName = "Lo")
    )

    private val mockRatingData = listOf(
        RatingData(rating = 5, reviewerId = "Reviewer1"),
        RatingData(rating = 4, reviewerId = "Reviewer2"),
        RatingData(rating = 4, reviewerId = "Reviewer3")
    )

    private val mockGenreData = listOf(
        GenreData(popularityMetric = 100, genreName = "Arabesk"),
        GenreData(popularityMetric = 31, genreName = "Pop"),
        GenreData(popularityMetric = 13, genreName = "Rock")
    )

    private val mockArtistData = listOf(
        ArtistData(releaseCount = 17, artistName = "Tarkan"),
        ArtistData(releaseCount = 10, artistName = "Yaşar"),
        ArtistData(releaseCount = 9, artistName = "Sezen Aksu")
    )

    private val mockSongData = listOf(
        SongData(popularityMetric = 5, songId = "1331"),
        SongData(popularityMetric = 4, songId = "1330"),
        SongData(popularityMetric = 3, songId = "1300")
    )

    fun fetchDataForOption(option: AnalysisOption) {
        when (option) {
            AnalysisOption.SongPopularity -> {
                val entryData = mockSongPopularityData.mapIndexed { index, data ->
                    FloatEntry(x = index.toFloat(), y = data.listens.toFloat())
                }
                Log.d("MyTag", "This is a debug")

                chartData.value = entryData
            }
            AnalysisOption.RatingDistribution -> {
                val entryData = mockRatingData.mapIndexed { index, data ->
                    FloatEntry(x = index.toFloat(), y = data.rating.toFloat())
                }
                chartData.value = entryData
            }
            AnalysisOption.PopularGenres -> {
                val entryData = mockGenreData.mapIndexed { index, data ->
                    FloatEntry(x = index.toFloat(), y = data.popularityMetric.toFloat())
                }
                chartData.value = entryData
            }
            AnalysisOption.ArtistProductivity -> {
                val entryData = mockArtistData.mapIndexed { index, data ->
                    FloatEntry(x = index.toFloat(), y = data.releaseCount.toFloat())
                }
                chartData.value = entryData
            }
            AnalysisOption.SongCharacteristics -> {
                val entryData = mockSongData.mapIndexed { index, data ->
                    FloatEntry(x = index.toFloat(), y = data.popularityMetric.toFloat())
                }
                chartData.value = entryData
            }
        }
    }
}
