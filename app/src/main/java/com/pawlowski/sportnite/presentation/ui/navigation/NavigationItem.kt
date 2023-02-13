package com.pawlowski.sportnite.presentation.ui.navigation

import com.pawlowski.utils.TestTag

data class NavigationItem(
    val text: String,
    val iconId: Int,
    val route: String,
    val testTag: TestTag
)