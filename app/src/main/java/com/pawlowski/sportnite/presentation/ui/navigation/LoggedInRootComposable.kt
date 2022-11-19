package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pawlowski.sportnite.R
import com.pawlowski.sportnite.presentation.ui.screens.HomeScreen
import com.pawlowski.sportnite.presentation.ui.screens.SportScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggedInRootComposable(
    onNavigateToSignInScreen: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntryState = navController.currentBackStackEntryAsState()
    val currentRoute = remember(navBackStackEntryState) {
        derivedStateOf {
            navBackStackEntryState.value?.destination?.route
        }
    }

    val navigationItems = listOf(
        NavigationItem(
            text = "Home",
            iconId = R.drawable.home_icon,
            route = "Home"
        )
    )
    val currentNavigationItem = remember {
        derivedStateOf {
            navigationItems.firstOrNull { it.route == currentRoute.value }
        }
    }




    Scaffold(bottomBar = {
        if(currentNavigationItem.value != null) {
            BottomBarNavigation(
                navigationItems = navigationItems,
                selectedItem = { currentNavigationItem.value },
                onNavigationItemClick = {
                    navController.navigate(it.route)
                    {
                        popUpTo("Home")
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }

    },
    floatingActionButton = {
        if(currentRoute.value == "Sport")
        {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = ""
                )
            }
        }
    }) { padding ->
        NavHost(navController = navController, startDestination = "Home") {
            composable("Home") {
                HomeScreen(
                    onNavigateToSportScreen = {
                        navController.navigate("Sport") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
            composable("Sport") {
                SportScreen(
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}