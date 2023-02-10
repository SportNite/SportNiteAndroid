package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pawlowski.accountdetails.ui.AccountDetailsScreen
import com.pawlowski.authentication.navigation.authenticationDestination
import com.pawlowski.choosesportsandlevels.navigation.chooseSportsAndLevelsDestination
import com.pawlowski.waitingforuserinfo.ui.WaitingForUserInfoScreen


@Composable
fun LoginNavigationGraph(
    navController: NavHostController,
    startDestination: String,
)
{
    NavHost(navController = navController, startDestination = startDestination)
    {
        authenticationDestination(
            navController = navController,
            onNavigateToNextScreen = {
                navController.navigate("WaitingForUserInfo")
                {
                    launchSingleTop = true
                    popUpTo("Authentication") {
                        inclusive = true
                    }
                }
            }
        )

        composable(route = "WaitingForUserInfo") {
            WaitingForUserInfoScreen(
                onNavigateToAccountDetailsScreen = {
                    navController.navigate("AccountDetails")
                    {
                        launchSingleTop = true
                        popUpTo("Authentication") {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHomeScreen = {
                    navController.navigate("LoggedInRoot") {
                        launchSingleTop = true
                        popUpTo("Authentication") {
                            inclusive = true
                        }
                    }
                },
                onNavigateToChooseSports = {
                    navController.navigate("ChooseSportAndLevels") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = "AccountDetails")
        {
            AccountDetailsScreen(
                onNavigateBack = { /*navController.popBackStack()*/ }, //TODO: add logout option
                onNavigateToNextScreen = {
                    navController.navigate("ChooseSportAndLevels") {
                        launchSingleTop = true
                        popUpTo("Authentication") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        chooseSportsAndLevelsDestination(
            navController = navController,
            onNavigateToNextScreen = {
                navController.navigate("LoggedInRoot") {
                    launchSingleTop = true
                    popUpTo("Authentication") {
                        inclusive = true
                    }
                }
            }
        )
        composable(route = "LoggedInRoot")
        {
            LoggedInRootComposable(onNavigateToSignInScreen = {
                navController.navigate("Authentication")
                {
                    launchSingleTop = true

                    popUpTo("LoggedInRoot") {
                        inclusive = true
                    }
                }
            })
        }
    }
}