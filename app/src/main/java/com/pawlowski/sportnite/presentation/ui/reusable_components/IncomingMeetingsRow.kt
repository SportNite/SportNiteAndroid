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
import com.pawlowski.sportnite.presentation.models.Meeting

@Composable
fun IncomingMeetingsRow(
    modifier: Modifier = Modifier,
    headerText: String = "Nadchodzące spotkania",
    headersPadding: PaddingValues = PaddingValues(),
    meetings: List<Meeting>?,
    displaySeeMore: Boolean = true,
    onMeetingCardClick: (Meeting) -> Unit = {},
    onSeeMoreClick: () -> Unit = {},
    isLoading: () -> Boolean = {false}
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.padding(headersPadding), verticalAlignment = Alignment.CenterVertically) {
            Text(text = headerText)
            Spacer(modifier = Modifier.weight(1f))
            if(displaySeeMore)
            {
                TextButton(onClick = { onSeeMoreClick() }) {
                    Text(text = "Zobacz wszystkie", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        if(!meetings.isNullOrEmpty()) {
            LazyRow(modifier= Modifier.fillMaxWidth()) {
                item { Spacer(modifier = Modifier.width(5.dp)) }
                items(meetings) {
                    MeetingCard(
                        modifier = Modifier.clickable { onMeetingCardClick(it) },
                        meeting = it
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

            }
        }
        else if(isLoading()) {
            CircularProgressIndicator(modifier = Modifier.size(30.dp))
        }
        else {
            //TODO
        }

    }
}