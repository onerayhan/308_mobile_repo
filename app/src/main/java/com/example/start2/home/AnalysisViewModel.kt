package com.example.start2.home

import AnalysisOption
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.start2.home.ui.ArtistData
import com.example.start2.home.ui.GenreData
import com.example.start2.home.ui.RatingData
import com.example.start2.home.ui.SongData
import com.example.start2.home.ui.SongPopularityData
import com.patrykandpatrick.vico.core.entry.FloatEntry







class AnalysisViewModel : ViewModel() {
    // LiveData to hold the chart data
    val chartData = MutableLiveData<List<FloatEntry>>()}


