package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController

@Composable
fun RootComposable(
    isUserLoggedIn: () -> Boolean
)
{
    val controller = rememberNavController()

    val isLoggedInValue = remember {
        isUserLoggedIn()
    }
    LoginNavigationGraph(navController = controller,
        startDestination = if(!isLoggedInValue)
            "SignIn"
        else
            "WaitingForUserInfo")
}