package com.pawlowski.authentication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pawlowski.authentication.ui.enter_sign_in_code_screen.EnterSignInCodeScreen
import com.pawlowski.authentication.ui.sign_in_screen.SignInScreen

fun NavGraphBuilder.authenticationDestination(
    navController: NavController,
    onNavigateToNextScreen: () -> Unit,
) {
    navigation(startDestination = "SignIn", route = "Authentication") {
        composable(route = "SignIn")
        {
            SignInScreen(onNavigateToEnterSignInCodeScreen = {
                navController.navigate("EnterCode")
                {
                    launchSingleTop = true
                }
            },
                onNavigateToNextScreen = onNavigateToNextScreen)
        }
        composable(route = "EnterCode")
        {
            EnterSignInCodeScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToNextScreen = onNavigateToNextScreen
            )
        }
    }
}