package com.example.start2.home.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun AnalysisScreen(
    navController: NavController
) {
    Scaffold {
            innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding) ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Home Screen", style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}