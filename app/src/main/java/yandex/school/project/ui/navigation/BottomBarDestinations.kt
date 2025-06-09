package yandex.school.project.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarDestinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Expenses : BottomBarDestinations(
        route = "expenses",
        title = "Расходы",
        icon = Icons.Filled.Home
    )
    object Income : BottomBarDestinations(
        route = "income",
        title = "Доходы",
        icon = Icons.Filled.Add
    )
    object Account : BottomBarDestinations(
        route = "account",
        title = "Счёт",
        icon = Icons.Filled.AccountBox
    )
    object Expenditure : BottomBarDestinations(
        route = "expenditure",
        title = "Траты",
        icon = Icons.Filled.ShoppingCart
    )
    object Settings : BottomBarDestinations(
        route = "settings",
        title = "Настройки",
        icon = Icons.Filled.Settings
    )
}