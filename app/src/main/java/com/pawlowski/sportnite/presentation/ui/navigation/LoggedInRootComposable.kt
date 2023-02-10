package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pawlowski.addoffer.ui.AddOfferScreen
import com.pawlowski.findplayerrs.ui.FindPlayersScreen
import com.pawlowski.fullscreenlist.ui.FullScreenListScreen
import com.pawlowski.home.ui.HomeScreen
import com.pawlowski.meetingdetails.ui.MeetingDetailsScreen
import com.pawlowski.module_notifications.ui.NotificationsScreen
import com.pawlowski.mymeetings.ui.MyMeetingsScreen
import com.pawlowski.playerdetails.ui.PlayerDetailsScreen
import com.pawlowski.settings.ui.SettingsScreen
import com.pawlowski.sharedresources.R
import com.pawlowski.sports.ui.SportScreen

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
        ),
        NavigationItem(
            text = "Szukaj ludzi",
            iconId = R.drawable.person_search_icon,
            route = "FindPlayers"
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
        if(screensWithButton.any { currentRoute.value?.contains(it) == true })
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
                        navController.navigate("Sport/${it.sportId}") {
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
                        navController.navigate("MeetingDetails/$it") {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToFullScreenList = {
                        navController.navigate("FullScreenList/$it") {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToNotificationsScreen = {
                        navController.navigate("Notifications") {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(
                route = "MyMeetings"
            ) {
                MyMeetingsScreen(
                    onNavigateToHomeScreen = {
                        navController.navigate("Home")
                        {
                            popUpTo("Home")
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    onNavigateToMeetingDetailsScreen = {
                        navController.navigate("MeetingDetails/$it") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
            composable(
                route = "Sport/{sportId}",
                arguments = listOf(
                    navArgument("sportId") {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) {
                SportScreen(
                    modifier = Modifier.padding(padding),
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToPlayerDetailsScreen = {
                        navController.navigate("PlayerDetails/$it") {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToMeetingDetailsScreen = {
                        navController.navigate("MeetingDetails/$it") {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToFindPlayersScreen = {
                        navController.navigate("FindPlayers") {
                            popUpTo("Home")
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    onNavigateToFullScreenList = { dataType, sportFilter ->
                        navController.navigate("FullScreenList/$dataType?sportFilter=${sportFilter.sportId}")
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
            composable(
                route = "MeetingDetails/{meetingId}",
                arguments = listOf(
                    navArgument("meetingId") {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) {
                MeetingDetailsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = "PlayerDetails/{playerId}",
                arguments = listOf(
                    navArgument(name = "playerId") {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) {
                PlayerDetailsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable("FindPlayers") {
                FindPlayersScreen(
                    modifier = Modifier.padding(padding),
                    onNavigateToHomeScreen = {
                        navController.navigate("Home")
                        {
                            popUpTo("Home")
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    onNavigateToPlayerDetailsScreen = {
                        navController.navigate("PlayerDetails/$it")
                    }
                )
            }
            composable(route = "FullScreenList/{dataType}?sportFilter={sportFilter}",
                arguments = listOf(
                    navArgument("dataType") {
                        type= NavType.StringType
                        nullable = false
                    },
                    navArgument("sportFilter") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )) {
                FullScreenListScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToSportScreen = {
                        navController.navigate("Sport/${it.sportId}") {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = "Notifications") {
                NotificationsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}