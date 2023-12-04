package com.example.start2.home.screens.info_screens

import DarkColorPalette
import LightColorPalette
import SectionCard
import Song
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import customFontFamily

data class Album(
    val name: String,
    val releaseDate: String,
    val songs: List<Song>
)

@Composable
fun AlbumScreen(album: Album) {
    MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = album.name,
                            fontFamily = customFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    backgroundColor = Color(0xFF2196F3)
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)) {

                // Album Release Date
                SectionHeader("Release Date")
                CustomText(
                    text = album.releaseDate,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Songs in Album
                SectionHeader("Songs")
                SectionCard {
                    LazyColumn {
                        items(album.songs) { song ->
                            CustomText(
                                text = "${song.name} - ${song.releaseDate}",
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun CustomText(text: String, fontWeight: FontWeight, fontSize: TextUnit) {
    Text(
        text = text,
        fontFamily = customFontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val mockSongs = listOf(
        Song("Birtanem", "2023"),
        Song("Kuşlar", "2023"),
        // Add more songs as needed
    )
    val mockAlbum = Album(
        name = "Yaşar Album",
        releaseDate = "2023",
        songs = mockSongs
    )


    AlbumScreen(album = mockAlbum)
}


