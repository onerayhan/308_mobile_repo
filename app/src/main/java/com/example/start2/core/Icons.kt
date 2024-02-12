package com.example.start2.core

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.start2.R
import com.example.start2.ThemedPreview


@Composable
fun defaultIconTint() = Color.Gray

@Composable
fun HomeIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector =  Icons.Outlined.Home,
        contentDescription = stringResource(id = R.string.home),
        modifier = modifier
    )
}

@Composable
fun SearchIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Outlined.Search,
        contentDescription = stringResource(id = R.string.search),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun FavoriteIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.FavoriteBorder,
        contentDescription = stringResource(id = R.string.favorites),
        tint = tint,
        modifier = modifier
    )
}
@Composable
fun RecommendationIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.AddTask,
        contentDescription = stringResource(id = R.string.favorites),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Outlined.Person,
        contentDescription = stringResource(id = R.string.profile),
        modifier = modifier
    )
}

@Composable
fun RateIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Outlined.StarOutline,
        contentDescription = stringResource(id = R.string.rate),
        modifier = modifier
    )
}
@Composable
fun AnalysisIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Outlined.AddChart,
        contentDescription = stringResource(id = R.string.analysis),
        modifier = modifier
    )
}


@Composable
fun ArrowBackIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.ArrowBack,
        contentDescription = stringResource(id = R.string.arrow_back),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun HeadPhonesIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_home),
        contentDescription = stringResource(id = R.string.arrow_back),
        modifier = modifier
    )
}

@Composable
fun BookIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_home),
        contentDescription = stringResource(id = R.string.arrow_back),
        modifier = modifier
    )
}

@Composable
fun ImportExportIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_home),
        contentDescription = "",
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun SortIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_home),
        contentDescription = "",
        tint = tint,
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun previewIcons() {
    ThemedPreview {
        FlowRow {
            HomeIcon()
            SearchIcon()
            FavoriteIcon()
            ProfileIcon()
            ImportExportIcon()
            SortIcon()
        }
    }
}

