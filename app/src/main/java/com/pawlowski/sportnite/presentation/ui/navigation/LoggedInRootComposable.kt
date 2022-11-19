package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pawlowski.sportnite.presentation.ui.screens.HomeScreen
import com.pawlowski.sportnite.presentation.ui.screens.SportScreen

@Composable
fun LoggedInRootComposable(
    onNavigateToSignInScreen: () -> Unit,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeScreen(
                onNavigateToSportScreen = {
                    navController.navigate("Sport") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("Sport") {
            SportScreen()
        }
    }
}