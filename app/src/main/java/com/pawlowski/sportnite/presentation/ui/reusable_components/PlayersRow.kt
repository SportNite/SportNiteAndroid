package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawlowski.sportnite.presentation.models.Player

@Composable
fun PlayersRow(
    modifier: Modifier = Modifier,
    headerText: String = "Inni gracze",
    headersPadding: PaddingValues = PaddingValues(),
    players: List<Player>?,
    displaySeeMore: Boolean = true,
    onPlayerCardClick: (Player) -> Unit,
    onSeeMoreClick: () -> Unit = {},
    isLoading: () -> Boolean = {false}
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
        LazyRow(
            modifier= Modifier.fillMaxWidth(),
            horizontalArrangement = if(isLoading())
                Arrangement.Center
            else
                Arrangement.Start
        ) {
            item { Spacer(modifier = Modifier.width(5.dp)) }
            if(!players.isNullOrEmpty())
            {
                items(players) {
                    PlayerCard(modifier = Modifier.clickable {
                        onPlayerCardClick(it)
                    }, player = it)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            else if(isLoading()) {
                item {
                    CircularProgressIndicator(Modifier.size(30.dp))
                }
            }

        }
    }
}