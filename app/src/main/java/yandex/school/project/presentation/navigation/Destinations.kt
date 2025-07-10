package yandex.school.project.presentation.navigation

import yandex.school.project.R

sealed class Destinations(
    val route: String,
    val title: Int
) {

    data object Account : Destinations(
        "account_screen",
        title = R.string.account_screen_title
    )

    data object Expenditure : Destinations(
        "expenditure_screen",
        title = R.string.expenditure_screen_title
    )

    data object Settings : Destinations(
        "settings_screen",
        title = R.string.settings_screen_title
    )
} 