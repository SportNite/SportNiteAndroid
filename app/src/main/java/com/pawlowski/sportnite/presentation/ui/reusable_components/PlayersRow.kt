package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.presentation.models.Player

@Composable
fun PlayersRow(
    modifier: Modifier = Modifier,
    headerText: String = "Inni gracze",
    headersPadding: PaddingValues = PaddingValues(),
    players: List<Player>?,
    displaySeeMore: Boolean = true,
    onSeeMoreClick: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.padding(headersPadding), verticalAlignment = Alignment.CenterVertically) {
            Text(text = headerText)
            Spacer(modifier = Modifier.weight(1f))
            if(displaySeeMore)
            {
                TextButton(onClick = { onSeeMoreClick() }) {
                    Text(text = "Zobacz wszystkich", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        LazyRow {
            item { Spacer(modifier = Modifier.width(5.dp)) }
            players?.let {
                items(players) {
                    PlayerCard(player = it)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

        }
    }
}