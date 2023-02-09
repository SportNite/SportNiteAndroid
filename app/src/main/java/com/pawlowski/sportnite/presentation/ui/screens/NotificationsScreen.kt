package com.pawlowski.sportnite.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.pawlowski.models.UserNotification
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.ui.reusable_components.DisappearingSwipeRefresh
import com.pawlowski.sportnite.presentation.ui.reusable_components.displayPagingItemsWithIndicators
import com.pawlowski.sportnite.presentation.view_models_related.notifications_screen.INotificationsViewModel
import com.pawlowski.sportnite.presentation.view_models_related.notifications_screen.NotificationsViewModel

@Composable
fun NotificationsScreen(
    viewModel: INotificationsViewModel = hiltViewModel<NotificationsViewModel>(),
    onNavigateBack: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val pagedNotifications = viewModel.pagetNotifications.collectAsLazyPagingItems()
        DisappearingSwipeRefresh(onRefresh = {
            pagedNotifications.refresh()
        }) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Powiadomienia")
                    IconButton(onClick = { onNavigateBack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(painter = painterResource(id = R.drawable.back_icon), contentDescription = "")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(modifier= Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    displayPagingItemsWithIndicators(
                        pagingItems = pagedNotifications
                    ) {
                        NotificationCard(
                            modifier = Modifier.padding(bottom = 8.dp),
                            notification = it
                        )
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun NotificationCard(
    modifier: Modifier = Modifier,
    notification: UserNotification,
) {
    val isExpanded = remember {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp)
    ) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = notification.tittle.asString(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Spacer(Modifier.width(6.dp))
                Text(
                    text = notification.date.asLocalDateTimeString()
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(targetState = isExpanded.value) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = notification.text.asString(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (isExpanded.value)
                            100
                        else
                            3
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(
                    onClick = { isExpanded.value = !isExpanded.value },

                    ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = ""
                    )
                }
            }
        }


    }

}