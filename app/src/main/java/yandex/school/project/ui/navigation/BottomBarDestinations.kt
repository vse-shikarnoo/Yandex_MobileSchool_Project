package yandex.school.project.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarDestinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Expenses: BottomBarDestinations(
        route = EXPENSES_ROUTE,
        title = "Expenses",
        icon = Icons.Default.Home
    )

    data object Settings: BottomBarDestinations(
        route = SETTINGS_ROUTE,
        title = "Settings",
        icon = Icons.Default.Menu
    )
}