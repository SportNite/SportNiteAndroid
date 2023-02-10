package com.pawlowski.commonui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

@Composable
fun LoginLottieAnimation(modifier: Modifier = Modifier)
{
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.pawlowski.sharedresources.R.raw.login_lottie_animation))
    val progress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
    )
    LottieAnimation(modifier = modifier
        .padding(top = 10.dp)
        .height(200.dp), composition = lottieComposition, progress = { progress })
}