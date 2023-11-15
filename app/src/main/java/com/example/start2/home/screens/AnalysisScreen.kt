package com.example.start2.home.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.start2.home.navigators.LeafScreen


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
            Text(text = "chartView",modifier = Modifier.clickable { navController?.navigateToLeafScreen(LeafScreen.AnalysisTable)})
        }
    }
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