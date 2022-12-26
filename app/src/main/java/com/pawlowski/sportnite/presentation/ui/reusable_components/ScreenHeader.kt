package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pawlowski.sportnite.R

@Composable
fun ScreenHeader(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onBackClick: () -> Unit = {}
) {
    Card(modifier  = modifier
        .fillMaxWidth()
        .height(150.dp), shape = RectangleShape
    ) {
        Box {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                model = imageUrl,
                contentDescription = ""
            )
            IconButton(modifier = Modifier.align(Alignment.TopStart), onClick = { onBackClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }
}