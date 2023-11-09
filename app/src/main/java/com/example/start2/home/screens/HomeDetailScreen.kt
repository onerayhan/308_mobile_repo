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
import androidx.compose.ui.unit.dp
import com.example.start2.core.ArrowBackIcon
import com.example.start2.core.FilledButton



@Composable
fun HomeDetailScreen(
    onBack: () -> Unit
) {
    Scaffold {
        innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            ArrowBackIcon(
                modifier = Modifier.padding(all = 24.dp),
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Home Detail Screen", style = MaterialTheme.typography.headlineMedium
            )
            FilledButton(
                modifier = Modifier
                    .padding(top = 100.dp)
                    .align(Alignment.Center),
                text = "Go Back",
                onClick = {
                    onBack()
                }
            )
        }
    }
}