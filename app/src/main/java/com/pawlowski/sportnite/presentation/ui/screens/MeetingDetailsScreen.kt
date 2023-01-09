package com.pawlowski.sportnite.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.view_models_related.meeting_details.IMeetingDetailsViewModel
import com.pawlowski.sportnite.presentation.view_models_related.meeting_details.MeetingDetailsViewModel
import com.pawlowski.sportnite.utils.dataOrNull

@Composable
fun MeetingDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: IMeetingDetailsViewModel = hiltViewModel<MeetingDetailsViewModel>(),
    onNavigateBack: () -> Unit = {},
) {
    val uiState = viewModel.container.stateFlow.collectAsState()
    val meetingState = remember {
        derivedStateOf {
            uiState.value.meeting.dataOrNull()
        }
    }
    Surface(modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(5.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Spotkanie")
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    onClick = onNavigateBack
                ) {
                    Icon(painter = painterResource(id = R.drawable.back_icon), contentDescription = "")
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "z:")
            Spacer(modifier = Modifier.height(5.dp))

            Card(modifier = Modifier.size(100.dp), shape = CircleShape) {
                meetingState.value?.opponent?.photoUrl?.let {
                    AsyncImage(model = it, contentDescription = "", contentScale = ContentScale.FillBounds)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            Text(text = meetingState.value?.opponent?.name ?: "")
            Spacer(modifier = Modifier.height(3.dp))

            Text(text = "${meetingState.value?.opponent?.age?: ""} lat")
            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "informacje:")
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = meetingState.value?.date?.asLocalDateTimeString()?:"")
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = "${meetingState.value?.city ?: ""}, ${meetingState.value?.placeOrAddress ?: ""}")
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "kontakt:")
            Spacer(modifier = Modifier.height(3.dp))

            Text(text = meetingState.value?.opponent?.phoneNumber?: "")
            Spacer(modifier = Modifier.height(10.dp))

            val context = LocalContext.current
            meetingState.value?.opponent?.phoneNumber?.let {  phoneValue ->
                Button(onClick = {
                    val smsUri: Uri = Uri.parse("smsto:$phoneValue") //Replace the phone number
                    val sms = Intent(Intent.ACTION_VIEW, smsUri)
                    context.startActivity(sms)
                }) {
                    Text(text = "Wyślij SMS")
                }
                Spacer(modifier = Modifier.height(5.dp))

                Button(onClick = {
                        //TODO
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text(text = "Anuluj spotkanie", color = Color.White)
                }
            }


        }
    }
}