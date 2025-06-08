package yandex.school.project.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import yandex.school.project.R

sealed class Screens(
    val route: String,
    val title: Int,
    val icon: ImageVector? = null) {
    data object Splash : Screens(
        route = "splash_screen",
        title = R.string.splash_screen_title
    )
    data object Expenses : Screens(
        "expenses_screen",
        title = R.string.expenses_screen_title
    )
    data object Income : Screens(
        "income_screen",
        title = R.string.income_screen_title
    )
    data object Account: Screens(
        "account_screen",
        title = R.string.account_screen_title
    )
    data object Expenditure: Screens(
        "expenditure_screen",
        title = R.string.expenditure_screen_title
    )
    data object Settings: Screens(
        "settings_screen",
        title = R.string.settings_screen_title
    )
}