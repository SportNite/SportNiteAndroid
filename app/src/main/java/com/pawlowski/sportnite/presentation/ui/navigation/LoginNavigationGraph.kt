package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pawlowski.sportnite.presentation.ui.screens.AccountDetailsScreen
import com.pawlowski.sportnite.presentation.ui.screens.EnterSignInCodeScreen
import com.pawlowski.sportnite.presentation.ui.screens.SignInScreen
import com.pawlowski.sportnite.presentation.ui.screens.WaitingForUserInfoScreen


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
            },
            onNavigateToWaitingForUserInfoScreen = {
                navController.navigate("WaitingForUserInfo")
                {
                    launchSingleTop = true
                    popUpTo("SignIn") {
                        inclusive = true
                    }
                }
            })
        }
        composable(route = "EnterCode")
        {
            EnterSignInCodeScreen(onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToWaitingForUserInfoScreen = {
                navController.navigate("WaitingForUserInfo")
                {
                    launchSingleTop = true
                    popUpTo("SignIn") {
                        inclusive = true
                    }
                }
            })
        }
        composable(route = "WaitingForUserInfo") {
            WaitingForUserInfoScreen(
                onNavigateToAccountDetailsScreen = {
                    navController.navigate("AccountDetails")
                    {
                        launchSingleTop = true
                        popUpTo("SignIn") {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHomeScreen = {
                    navController.navigate("LoggedInRoot") {
                        launchSingleTop = true
                        popUpTo("SignIn") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = "AccountDetails")
        {
            AccountDetailsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNextScreen = {
                    navController.navigate("LoggedInRoot") {
                        launchSingleTop = true
                        popUpTo("SignIn") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = "LoggedInRoot")
        {
            LoggedInRootComposable(onNavigateToSignInScreen = {
                navController.navigate("SignIn")
                {
                    launchSingleTop = true
                    popUpTo("SignIn") {
                        inclusive = true
                    }
                }
            })
        }
    }
}