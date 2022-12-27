package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawlowski.sportnite.presentation.models.GameOffer

fun LazyListScope.gameOffersColumnItem(
    offers: List<GameOffer>,
    headerText: String,
    headersPadding: PaddingValues = PaddingValues(),
    displaySeeMore: Boolean = true,
    onSeeMoreClick: () -> Unit = {},
    onOfferTextButtonClick: (GameOffer) -> Unit = {},
) {
    item {
        Row(modifier = Modifier.padding(headersPadding), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = headerText
            )
            Spacer(modifier = Modifier.weight(1f))
            if(displaySeeMore)
            {
                TextButton(onClick = { onSeeMoreClick() }) {
                    Text(
                        text = "Zobacz wszystkie",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }
    }
    item {
        Spacer(modifier = Modifier.height(5.dp))
    }
    items(offers) {
        Spacer(modifier = Modifier.height(8.dp))
        val isExpanded = rememberSaveable(offers) {
            mutableStateOf(false)
        }
        GameOfferCard(
            gameOffer = it,
            isExpanded = { isExpanded.value },
            onExpandClick = {
                isExpanded.value = !isExpanded.value
            },
            onTextButtonClick = {
                onOfferTextButtonClick(it)
            }
        )
    }
}