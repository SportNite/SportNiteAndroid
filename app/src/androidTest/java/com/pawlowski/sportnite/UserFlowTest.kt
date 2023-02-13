package com.pawlowski.sportnite

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.pawlowski.sportnite.presentation.ui.navigation.LoggedInRootComposable
import com.pawlowski.sportnite.utils.assertCurrentRouteContains
import com.pawlowski.utils.TestTag
import com.pawlowski.utils.TestTag.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class UserFlowTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            LoggedInRootComposable(
                onNavigateToSignInScreen = {
                    /*TODO*/
                },
                navController = navController
            )

        }
    }

    @Test
    fun startDestination_equalsHome() {
        composeTestRule
            .onNodeWithTag(NAVIGATION_HOME.name)
            .assertIsDisplayed()

        navController.assertCurrentRouteContains(expectedRoute = "Home")
    }

    @Test
    fun verifyBottomNavBarNavigation() {
        performClick(NAVIGATION_MY_MEETINGS)
        navController.assertCurrentRouteContains(expectedRoute = "MyMeetings")

        performClick(NAVIGATION_FIND_PLAYERS)
        navController.assertCurrentRouteContains(expectedRoute = "FindPlayers")

        performClick(NAVIGATION_HOME)
        navController.assertCurrentRouteContains(expectedRoute = "Home")

    }

    @Test
    fun verifyNavigateToSportScreen() {
        performClick(SPORT_CARD)
        navController.assertCurrentRouteContains(expectedRoute = "Sport")

    }

    private fun performClick(testTag: TestTag) {
        composeTestRule
            .onNodeWithTag(testTag.name)
            .assertIsDisplayed()
            .performClick()
    }
}