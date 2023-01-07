package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun NoItemsFoundCard()
{
    Column(horizontalAlignment = CenterHorizontally) {
        NoItemsFoundAnimation()
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
            text = "Nie znaleziono wyników dla podanych kryteriów wyszukiwania",
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoItemsFoundAnimation()
{
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.pawlowski.sportnite.R.raw.search_not_found_animation))
    val progress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
    )
    LottieAnimation(modifier = Modifier
        .padding(10.dp)
        .height(180.dp), composition = lottieComposition, progress = { progress })
}