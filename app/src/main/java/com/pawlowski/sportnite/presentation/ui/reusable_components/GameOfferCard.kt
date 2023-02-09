package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pawlowski.sportnite.R
import com.pawlowski.models.GameOffer
import com.pawlowski.models.mappers.getGameOfferForPreview

@Composable
fun GameOfferCard(
    gameOffer: GameOffer,
    isExpanded: () -> Boolean = {false},
    textButtonText: @Composable () -> Unit,
    onTextButtonClick: () -> Unit = {},
    cancelButton: (@Composable (GameOffer) -> Unit)? = null,
    onExpandClick: (GameOffer) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
    ) {
        Row(
            modifier = Modifier.height(55.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.person_icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(modifier = Modifier.weight(1f), text = gameOffer.owner.name)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(id = R.drawable.calendar_icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = gameOffer.date.asLocalDateTimeString())
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = { onExpandClick(gameOffer) }) {
                Icon(painter = painterResource(id = R.drawable.arrow_down), contentDescription = "")
            }
        }
        AnimatedVisibility(visible = isExpanded()) {
            Column(modifier = Modifier
                    .fillMaxWidth()
                ) {
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Dodatkowe informacje:", fontWeight = FontWeight.SemiBold)
                        Text(text = gameOffer.additionalNotes)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    //VerticalDivider()
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Miejsce:", fontWeight = FontWeight.SemiBold)
                        Text(text = "${gameOffer.placeOrAddress}, ${gameOffer.city}")
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                }
                Spacer(modifier = Modifier.height(10.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    cancelButton?.invoke(gameOffer)
                    TextButton(
                        onClick = onTextButtonClick
                    ) {
                        textButtonText()
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameOfferCardPreview() {
    GameOfferCard(
        getGameOfferForPreview(),
        textButtonText = {
            Text(text = "Akceptuj propozycję")
        },
        isExpanded = {true},
        cancelButton = {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Odrzuć propozycję", color = Color.Red)
            }
        }
    )
}