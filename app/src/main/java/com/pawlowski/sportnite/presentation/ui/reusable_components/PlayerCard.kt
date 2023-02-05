package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pawlowski.models.Player
import com.pawlowski.models.mappers.getPlayerForPreview

@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Player
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .width(166.dp),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .weight(1f),
                model = player.photoUrl,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = player.name, fontWeight = FontWeight.Medium)
            Text(text = "${player.age} lat")
            //Text(text = player.advanceLevel.asString)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerCardPreview() {
    PlayerCard(player = getPlayerForPreview())
}