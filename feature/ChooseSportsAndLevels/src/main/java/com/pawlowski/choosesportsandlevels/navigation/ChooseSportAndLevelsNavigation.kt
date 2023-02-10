package com.pawlowski.choosesportsandlevels.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pawlowski.choosesportsandlevels.ui.choose_advance_level_screen.ChooseAdvanceLevelScreen
import com.pawlowski.choosesportsandlevels.ui.choose_sports_screen.ChooseSportsScreen


fun NavGraphBuilder.chooseSportsAndLevelsDestination(
    navController: NavController,
    onNavigateToNextScreen: () -> Unit,
) {
    navigation(startDestination = "ChooseSports", route = "ChooseSportAndLevels") {
        composable(route = "ChooseSports") {
            ChooseSportsScreen(onNavigateToChoseAdvanceLevelScreen = {
                navController.navigate("ChooseAdvanceLevel") {
                    launchSingleTop = true
                }
            })
        }
        composable(route = "ChooseAdvanceLevel") {
            ChooseAdvanceLevelScreen(
                onNavigateToNextScreen = {
                    onNavigateToNextScreen()
                },
                onNavigateToChooseSportsScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}