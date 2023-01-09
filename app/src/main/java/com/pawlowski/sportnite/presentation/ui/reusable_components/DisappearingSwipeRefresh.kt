package com.pawlowski.sportnite.presentation.ui.reusable_components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DisappearingSwipeRefresh(
    onRefresh: () -> Unit,
    disappearingTime: Long = 3000,
    content: @Composable () -> Unit
) {
    val isRefreshingState = remember {
        mutableStateOf(false)
    }
    val refreshScope = rememberCoroutineScope()

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshingState.value), onRefresh = {
        refreshScope.launch {
            isRefreshingState.value = true
            onRefresh()
            delay(disappearingTime)
            isRefreshingState.value = false
        }
    }) {
        content()
    }
}