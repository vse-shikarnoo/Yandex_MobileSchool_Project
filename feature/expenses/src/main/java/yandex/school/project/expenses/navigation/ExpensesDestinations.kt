package yandex.school.project.expenses.navigation

import yandex.school.project.expenses.R

const val EXPENSES_ROUTE = "expenses"

sealed class ExpensesDestinations(
    val route: String,
    val title: Int
) {
    data object ExpensesScreen : ExpensesDestinations(
        EXPENSES_ROUTE + "_screen",
        title = R.string.expenses_graph_title
    )

    data object ExpensesCreateScreen : ExpensesDestinations(
        EXPENSES_ROUTE + "_create_screen",
        title = R.string.expenses_create_title
    )

    data object ExpensesHistoryScreen : ExpensesDestinations(
        EXPENSES_ROUTE + "_history_screen",
        title = R.string.expenses_history_title
    )

    data object ExpensesAnalyticScreen : ExpensesDestinations(
        EXPENSES_ROUTE + "_analytic_screen",
        title = R.string.expenses_analytic_title
    )
}