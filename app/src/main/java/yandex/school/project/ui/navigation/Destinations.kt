package yandex.school.project.ui.navigation

import yandex.school.project.R

const val EXPENSES_ROUTE = "expenses"

sealed class Destinations(
    val route: String,
    val title: Int
) {

    data object Splash : Destinations(
        route = "splash_screen",
        title = R.string.splash_screen_title
    )


    data object ExpensesScreen : Destinations(
        EXPENSES_ROUTE + "_screen",
        title = R.string.expenses_graph_title
    )

    data object ExpensesCreateScreen : Destinations(
        EXPENSES_ROUTE + "_create_screen",
        title = R.string.expenses_create_title
    )

    data object ExpensesHistoryScreen : Destinations(
        EXPENSES_ROUTE + "s_history_screen",
        title = R.string.expenses_history_title
    )

    data object ExpensesAnalyticScreen : Destinations(
        EXPENSES_ROUTE + "_analytic_screen",
        title = R.string.expenses_analytic_title
    )


    data object Income : Destinations(
        "income_screen",
        title = R.string.income_screen_title
    )

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