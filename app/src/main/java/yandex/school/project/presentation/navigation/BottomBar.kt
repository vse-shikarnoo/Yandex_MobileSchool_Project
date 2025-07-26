package yandex.school.project.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalContext
import yandex.school.project.core.data.local.LocalUserPreferences
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Нижняя панель навигации приложения.
 * Единственная ответственность: отображение и управление нижней панелью навигации между экранами.
 */
@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        Pair(BottomBarDestinations.Expenses, "Expenses"),
        Pair(BottomBarDestinations.Incomes, "Incomes"),
        Pair(BottomBarDestinations.Account, "Account"),
        Pair(BottomBarDestinations.Expenditure, "Categories"),
        Pair(BottomBarDestinations.Settings, "Settings")
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val userPrefs = LocalUserPreferences.current
    val haptic = LocalHapticFeedback.current

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                modifier = Modifier.testTag(screen.second),
                label = {
                    Text(
                        text = screen.first.title
                    )
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(screen.first.icon),
                        contentDescription = null
                    )
                },
                selected = currentRoute == screen.first.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = {
                    if (userPrefs.hapticsEnabled) {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    }
                    navController.navigate(screen.first.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
} 