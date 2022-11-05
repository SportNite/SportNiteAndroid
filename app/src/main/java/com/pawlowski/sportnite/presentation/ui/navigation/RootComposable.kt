package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootComposable(
    isUserLoggedIn: () -> Boolean
)
{
    val controller = rememberNavController()
    LoginNavigationGraph(navController = controller,
        startDestination = if(!isUserLoggedIn())
            "SignIn"
        else
            "LoggedInRoot")
}