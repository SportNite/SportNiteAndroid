package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextDivider(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = text,
            //color = MidGrey,
            fontSize = 13.sp,
            //fontFamily = montserratFont
        )
        Divider(modifier = Modifier.weight(1f))
    }
}