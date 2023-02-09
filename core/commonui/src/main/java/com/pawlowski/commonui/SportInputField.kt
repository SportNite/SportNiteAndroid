package com.pawlowski.commonui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pawlowski.sharedresources.R
import com.pawlowski.models.Sport

@Composable
fun SportInputField(
    modifier: Modifier = Modifier,
    chosenSport: Sport?,
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.extraSmall,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        border = BorderStroke(width = 0.2.dp, Color.DarkGray)
    ) {
        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))

            Icon(

                painter = painterResource(id = R.drawable.sport_icon),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = chosenSport?.sportName?.asString() ?:"Kliknij aby wybrać sport",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(id = R.drawable.dropdown_arrow_icon), contentDescription = "")
            Spacer(modifier = Modifier.width(15.dp))

        }
    }
}