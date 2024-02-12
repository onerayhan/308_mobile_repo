package com.example.start2.viewmodels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.start2.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip


@Composable
fun AndroidLarge1(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 800.dp)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 255.dp)
                .background(color = Color(0xff1d1d20)))


        Text(
            text = "username",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = (-81).dp,
                    y = 88.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.rectangle_14),
            contentDescription = "Rectangle 14",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 188.dp,
                    y = 193.dp
                )
                .requiredWidth(width = 143.dp)
                .requiredHeight(height = 44.dp)
                .clip(shape = RoundedCornerShape(15.dp)))
        Text(
            text = "Share profile",
            color = Color(0xff5ec269),
            style = TextStyle(
                fontSize = 18.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 201.dp,
                    y = 202.dp
                )
                .requiredWidth(width = 128.dp)
                .requiredHeight(height = 26.dp))
        Image(
            painter = painterResource(id = R.drawable.rectangle_6),
            contentDescription = "Rectangle 6",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 27.5.dp,
                    y = 193.5.dp
                )
                .requiredWidth(width = 140.dp)
                .requiredHeight(height = 44.dp)
                .clip(shape = RoundedCornerShape(15.dp)))
        Text(
            text = "Edit profile",
            color = Color(0xff5ec269),
            style = TextStyle(
                fontSize = 18.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 44.dp,
                    y = 204.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.rectangle_15),
            contentDescription = "Rectangle 15",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = (-3).dp,
                    y = 255.dp
                )
                .requiredWidth(width = 363.dp)
                .requiredHeight(height = 545.dp))
        Image(
            painter = painterResource(id = R.drawable.rectangle_12__1_),
            contentDescription = "Rectangle 12",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 43.dp,
                    y = 588.dp
                )
                .requiredWidth(width = 271.dp)
                .requiredHeight(height = 48.dp)
                .clip(shape = RoundedCornerShape(16.dp)))
        Text(
            text = "Search friend:",
            color = Color.White,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 53.dp,
                    y = 600.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.rectangle_16),
            contentDescription = "Rectangle 16",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 198.dp,
                    y = 282.dp
                )
                .requiredWidth(width = 124.dp)
                .requiredHeight(height = 37.dp)
                .clip(shape = RoundedCornerShape(13.dp)))
        Text(
            text = "Following :",
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 208.dp,
                    y = 292.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.rectangle_9),
            contentDescription = "Rectangle 9",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 37.dp,
                    y = 282.dp
                )
                .requiredWidth(width = 131.dp)
                .requiredHeight(height = 37.dp)
                .clip(shape = RoundedCornerShape(13.dp)))
        Text(
            text = "Follower :",
            color = Color.White,
            style = TextStyle(
                fontSize = 16.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 53.dp,
                    y = 293.dp
                ))
        Text(
            text = "More",
            color = Color(0xff5fc36a),
            style = TextStyle(
                fontSize = 16.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 240.dp,
                    y = 369.dp
                )
                .requiredWidth(width = 74.dp)
                .requiredHeight(height = 20.dp))
        Text(
            text = "Top tracks",
            color = Color.White,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 25.dp,
                    y = 369.dp
                )
                .requiredWidth(width = 180.dp)
                .requiredHeight(height = 40.dp))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun AndroidLarge1Preview() {
    AndroidLarge1()
}
