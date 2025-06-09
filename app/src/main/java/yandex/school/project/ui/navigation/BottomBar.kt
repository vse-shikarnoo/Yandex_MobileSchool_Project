package yandex.school.project.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState



@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarDestinations.Expenses,
        BottomBarDestinations.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinations = navBackStackEntry?.destination

     NavigationBar {
        screens.forEach {
            AddItem(
                screen = it,
                currentDestinations = currentDestinations,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarDestinations,
    currentDestinations: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Nav Icon"
            )
        },
        selected = currentDestinations?.hierarchy?.any { it.route == screen.route } == true,
        colors = NavigationBarItemDefaults.colors(
            unselectedTextColor = LocalContentColor.current.copy(
                alpha = 0.4f
            ),
            unselectedIconColor = LocalContentColor.current.copy(
                alpha = 0.4f
            )
        ),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}