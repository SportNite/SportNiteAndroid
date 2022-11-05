package com.pawlowski.sportnite.presentation.ui.reusable_components

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
import com.pawlowski.sportnite.R

@Composable
fun DateInputField(
    modifier: Modifier = Modifier,
    dateText: String = "",
    onClick: () -> Unit,
    icon: @Composable () -> Unit = { Icon(painter = painterResource(id = R.drawable.cake_icon), contentDescription = "") }
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
            icon()
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = dateText, fontSize = 16.sp)
        }
    }
}