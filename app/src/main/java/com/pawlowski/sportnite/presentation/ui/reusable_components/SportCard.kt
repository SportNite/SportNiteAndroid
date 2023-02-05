package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pawlowski.models.Sport

@Composable
fun SportCard(
    modifier: Modifier = Modifier,
    sport: Sport,
    onSportClick: (Sport) -> Unit ={}
) {
    Card(modifier = modifier
        .height(80.dp)
        .width(130.dp)
        .clickable {
            onSportClick(sport)
        }) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = sport.sportBackgroundUrl,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
            Text(
                modifier= Modifier.align(Alignment.BottomCenter),
                text = sport.sportName.asString(),
                color = Color.White
            )
        }
    }
}