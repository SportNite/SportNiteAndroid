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
import com.pawlowski.sportnite.presentation.ui.screens.*

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
        ),
        NavigationItem(
            text = "Moje spotkania",
            iconId = R.drawable.my_meetings_icon,
            route = "MyMeetings"
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
        val screensWithButton = listOf("Sport", "MyMeetings")
        if(screensWithButton.contains(currentRoute.value))
        {
            FloatingActionButton(onClick = {
                navController.navigate("AddOffer") {
                    launchSingleTop = true
                }
            }) {
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
                    modifier = Modifier.padding(padding),
                    onNavigateToSettingsScreen = {
                        navController.navigate("Settings") {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToMeetingDetails = {
                        navController.navigate("MeetingDetails") {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable("MyMeetings") {
                MyMeetingsScreen(
                    onNavigateToHomeScreen = {
                        navController.navigate("Home")
                        {
                            popUpTo("Home")
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable("Sport") {
                SportScreen(
                    modifier = Modifier.padding(padding),
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToPlayerDetailsScreen = {
//                        navController.navigate("PlayerDetails") {
//                            launchSingleTop = true
//                        }
                    },
                    onNavigateToMeetingDetailsScreen = {
                        navController.navigate("MeetingDetails") {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable("AddOffer") {
                AddOfferScreen(onNavigateBack = {
                    navController.popBackStack()
                })
            }
            composable("Settings") {
                SettingsScreen(
                    onNavigateToLoginScreen = onNavigateToSignInScreen
                )
            }
            composable("MeetingDetails") {
                MeetingDetailsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}