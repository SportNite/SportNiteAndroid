package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
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