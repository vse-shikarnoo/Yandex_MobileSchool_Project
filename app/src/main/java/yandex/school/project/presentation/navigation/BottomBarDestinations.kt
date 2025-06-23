package yandex.school.project.presentation.navigation

import yandex.school.project.R

sealed class BottomBarDestinations(
    val route: String,
    val title: String,
    val icon: Int
) {
    data object Expenses : BottomBarDestinations(
        route = "expenses",
        title = "Расходы",
        icon = R.drawable.ic_expenses
    )
    data object Incomes : BottomBarDestinations(
        route = "incomes",
        title = "Доходы",
        icon = R.drawable.ic_income
    )
    data object Account : BottomBarDestinations(
        route = "account",
        title = "Счёт",
        icon = R.drawable.ic_account
    )
    data object Expenditure : BottomBarDestinations(
        route = "category",
        title = "Статья",
        icon = R.drawable.ic_category
    )
    data object Settings : BottomBarDestinations(
        route = "settings",
        title = "Настройки",
        icon = R.drawable.ic_settings
    )
} 