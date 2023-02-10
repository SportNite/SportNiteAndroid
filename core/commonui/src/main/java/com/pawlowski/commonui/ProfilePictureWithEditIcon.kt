package com.pawlowski.commonui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pawlowski.sharedresources.R

@Composable
fun ProfilePictureWithEditIcon(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    photoUrl: String? = null,
) {
    Box(modifier = modifier.size(size)) {
        Card(modifier = Modifier.fillMaxSize(), shape = CircleShape) {
            photoUrl?.let {
                AsyncImage(model = photoUrl, contentDescription = "")
            }
        }
        Card(shape = MaterialTheme.shapes.extraSmall, modifier = Modifier.align(Alignment.BottomEnd), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Icon(modifier = Modifier.padding(3.dp), painter = painterResource(id = R.drawable.edit_icon), contentDescription = "", tint = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePictureWithEditIconPreview() {
    ProfilePictureWithEditIcon()
}