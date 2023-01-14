package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.sportnite.presentation.models.Meeting
import com.pawlowski.sportnite.presentation.ui.utils.getMeetingForPreview

@Composable
fun MeetingCard(
    modifier: Modifier = Modifier,
    meeting: Meeting
) {
    Card(
        modifier = modifier
            .width(130.dp)
            .height(75.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = meeting.opponent.name, fontSize = 10.sp)
                Text(text = meeting.sport.sportName.asString(), fontSize = 10.sp)
                Text(text = meeting.placeOrAddress, fontSize = 10.sp)
                Text(text = meeting.city, fontSize = 10.sp)
                Text(text = meeting.date.asLocalDateTimeString(), fontSize = 10.sp)
            }
            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 5.dp, bottom = 5.dp)
                    .size(14.dp),
                painter = painterResource(
                    id = meeting.sport.sportIconId
                ),
                contentDescription = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeetingCardPreview() {
    MeetingCard(
        meeting = getMeetingForPreview()
    )
}