package com.pawlowski.sportnite.utils

import androidx.navigation.NavHostController
import com.google.common.truth.Truth.assertThat

fun NavHostController.currentRoute(): String? {
    return currentBackStackEntry?.destination?.route
}

fun NavHostController.assertCurrentRouteContains(expectedRoute: String) {
    return assertThat(currentRoute()).contains(expectedRoute)
}