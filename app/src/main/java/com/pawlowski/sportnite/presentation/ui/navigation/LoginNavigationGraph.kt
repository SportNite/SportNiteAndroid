package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pawlowski.sportnite.presentation.ui.screens.AccountDetailsScreen
import com.pawlowski.sportnite.presentation.ui.screens.EnterSignInCodeScreen
import com.pawlowski.sportnite.presentation.ui.screens.SignInScreen


@Composable
fun LoginNavigationGraph(
    navController: NavHostController,
    startDestination: String,
)
{
    NavHost(navController = navController, startDestination = startDestination)
    {
        composable(route = "SignIn")
        {
            SignInScreen(onNavigateToEnterSignInCodeScreen = {
                navController.navigate("EnterCode")
                {
                    launchSingleTop = true
                }
            })
        }
        composable(route = "EnterCode")
        {
            EnterSignInCodeScreen(onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToAccountDetailsScreen = {
                navController.navigate("AccountDetails")
            })
        }
        composable(route = "AccountDetails")
        {
            AccountDetailsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(route = "LoggedInRoot")
        {

        }
    }
}