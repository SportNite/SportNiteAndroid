package com.pawlowski.sportnite.presentation.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource

@Composable
fun BottomBarNavigation(
    navigationItems: List<NavigationItem>,
    selectedItem: () -> NavigationItem?,
    onNavigationItemClick: (NavigationItem) -> Unit
) {


    NavigationBar {
        navigationItems.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.testTag(item.testTag.name),
                selected = selectedItem() == item,
                onClick = { onNavigationItemClick(item) },
                label = { Text(text = item.text) },
                icon = { Icon(painter = painterResource(id = item.iconId), contentDescription = "") }
            )
        }
    }
}