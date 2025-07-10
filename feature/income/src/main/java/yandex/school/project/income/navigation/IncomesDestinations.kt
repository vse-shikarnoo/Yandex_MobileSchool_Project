package yandex.school.project.income.navigation


import yandex.school.project.incomes.R

const val INCOMES_ROUTE = "incomes"

sealed class IncomesDestinations(
    val route: String,
    val title: Int
) {
    data object IncomesScreen : IncomesDestinations(
        INCOMES_ROUTE + "_screen",
        title = R.string.expenses_graph_title
    )

    data object IncomesCreateScreen : IncomesDestinations(
        INCOMES_ROUTE + "_create_screen",
        title = R.string.expenses_create_title
    )

    data object IncomesHistoryScreen : IncomesDestinations(
        INCOMES_ROUTE + "_history_screen",
        title = R.string.expenses_history_title
    )

    data object IncomesAnalyticScreen : IncomesDestinations(
        INCOMES_ROUTE + "_analytic_screen",
        title = R.string.expenses_analytic_title
    )
}