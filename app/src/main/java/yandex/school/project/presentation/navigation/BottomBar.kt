package yandex.school.project.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Нижняя панель навигации приложения.
 * Единственная ответственность: отображение и управление нижней панелью навигации между экранами.
 */
@Composable
fun BottomBar(navController: NavHostController, hapticsEnabled: Boolean = true) {
    val screens = listOf(
        BottomBarDestinations.Expenses,
        BottomBarDestinations.Incomes,
        BottomBarDestinations.Account,
        BottomBarDestinations.Expenditure,
        BottomBarDestinations.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val haptic = LocalHapticFeedback.current

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(
                        text = screen.title
                    )
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(screen.icon),
                        contentDescription = null
                    )
                },
                selected = currentRoute == screen.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = {
                    if (hapticsEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
} 