package yandex.school.project.ui.navigation

import yandex.school.project.R

sealed class BottomBarDestinations(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Expenses : BottomBarDestinations(
        route = "expenses",
        title = "Расходы",
        icon = R.drawable.ic_expenses
    )
    object Income : BottomBarDestinations(
        route = "income",
        title = "Доходы",
        icon = R.drawable.ic_income
    )
    object Account : BottomBarDestinations(
        route = "account",
        title = "Счёт",
        icon = R.drawable.ic_account
    )
    object Expenditure : BottomBarDestinations(
        route = "category",
        title = "Статья",
        icon = R.drawable.ic_category
    )
    object Settings : BottomBarDestinations(
        route = "settings",
        title = "Настройки",
        icon = R.drawable.ic_settings
    )
}