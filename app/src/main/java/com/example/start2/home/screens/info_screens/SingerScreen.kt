import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController
import com.example.start2.R
import com.example.start2.home.spotify.SpotifyViewModel

//TODO:: connect it to the navHost

data class Singer(val name: String)
data class Song(val name: String, val releaseDate: String)
data class Album(val name: String, val releaseDate: String)
data class Single(val name: String, val releaseDate: String)
data class AppearsOn(val name: String, val releaseDate: String)

val customFontFamily = FontFamily(
    Font(R.font.opensans_semicondensed_mediumitalic),
    Font(R.font.opensans_semicondensed_mediumitalic, FontWeight.Bold)
)
@Composable
fun SingerScreen(navController: NavController, spotifyViewModel: SpotifyViewModel) {
    spotifyViewModel.getSelectedArtist()

    MaterialTheme(colors = if(isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)) {
            //SingerContent()
            val selectedArtistInfo by spotifyViewModel.selectedArtistInfo.observeAsState()
            Log.d("RegistrationActivity", spotifyViewModel._selectedArtistID.value.toString())
            selectedArtistInfo.let { infoResponse ->
                Log.d("RegistrationActivity", infoResponse?.href.toString())
                Column{

                    SingerContent(singerName = infoResponse?.name)
                }

            }

        }
    }
}

/*


@Composable
fun SingerScreen(singer: Singer, topSongs: List<Song>, albums: List<Album>, singles: List<Single>, appearsOn: List<AppearsOn>) {
    MaterialTheme(colors = if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)) {

            SingerContent(singer, topSongs, albums, singles, appearsOn)
        }
    }
}*/
@Composable
fun SingerContent(
    singerName: String?,
) {
    // Singer's Name with Custom Font
    CustomText(text = "a $singerName", fontWeight = FontWeight.Bold, fontSize = 30.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}

@Composable
fun SingerContent(
    singer: Singer,
    topSongs: List<Song>,
    albums: List<Album>,
    singles: List<Single>,
    appearsOn: List<AppearsOn>
) {
    // Singer's Name with Custom Font
    CustomText(text = singer.name, fontWeight = FontWeight.Bold, fontSize = 30.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

    // Section for Top Songs
    SectionHeader("Top Songs")
    Divider(color = Color.Gray, thickness = 5.dp)
    SectionCard {
        LazyColumn {
            items(topSongs) { song ->
                Text("${song.name} - ${song.releaseDate}", color = MaterialTheme.colors.onBackground)
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Section for Albums
    SectionHeader("Albums")
    Divider(color = Color.Gray, thickness = 5.dp)
    SectionCard {
        LazyColumn {
            items(albums) { album ->
                AlbumItem(album = album) {
                    // TODO: Handle album click
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Section for Singles & EPs
    SectionHeader("Singles & EPs")
    Divider(color = Color.Gray, thickness = 5.dp)
    SectionCard {
        LazyColumn {
            items(singles) { single ->
                Text("${single.name} - ${single.releaseDate}", color = MaterialTheme.colors.onBackground)
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Section for Appears On
    SectionHeader("Appears On")
    Divider(color = Color.Gray, thickness = 5.dp)
    SectionCard {
        LazyColumn {
            items(appearsOn) { appearance ->
                Text("${appearance.name} - ${appearance.releaseDate}", color = MaterialTheme.colors.onBackground)
            }
        }
    }
}





@Composable
fun CustomText(text: String, fontWeight: FontWeight, fontSize: TextUnit, modifier: Modifier = Modifier, textAlign: TextAlign? = null) {
    Text(
        text = text,
        fontFamily = customFontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        modifier = modifier,
        textAlign = textAlign,
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,  // Adjust the font size as needed
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}


@Composable
fun SectionCard(content: @Composable () -> Unit) {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(8.dp)
    ) {
        content()
    }
}

@Composable
fun SectionDivider() {
    Divider(color = Color.Gray, thickness = 1.dp)
}



val mockSinger = Singer("TELYKAST")

val mockTopSongs = listOf(
    Song("Somebody New", "2020"),
    Song("DAYLIGHT", "2019"),
    Song("Nobody To Love", "2010") ,
    Song("Unbreakable", "2021")

)

val mockAlbums = listOf(
    Album("Better Now", "2029"),
    Album("Somebody New", "2021"),
    Album("Come Back/ Filthy", "2020"),
    Album("Better", "2019")

)

val mockSingles = listOf(
    Single("Desire - Single", "2023"),
    Single("Electric Feeling - Single", "2023"),
    Single("You Got Me (Remixes)", "2023"),
    Single("Move It(with Luciana)", "2023")

    )


val mockAppearances = listOf(
    AppearsOn("No one else", "2022"),
    AppearsOn("Worth Fighting For", "2019")
)

val DarkColorPalette = darkColors(
    primary = Color(0xFFFFA500), // Orange
    background = Color.Black,
    onBackground = Color.White,
    // Other colors...
)

val LightColorPalette = lightColors(
    primary = Color(0xFFFFA500), // Orange
    background = Color.White,
    onBackground = Color.Black,
    // Other colors...
)





@Composable
fun AlbumItem(album: Album, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = album.name,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = album.releaseDate,
            color = MaterialTheme.colors.onBackground
        )
    }
}
/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SingerScreen(mockSinger, mockTopSongs, mockAlbums, mockSingles, mockAppearances)
}*/




