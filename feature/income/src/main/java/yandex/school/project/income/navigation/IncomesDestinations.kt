package yandex.school.project.income.navigation


import yandex.school.project.incomes.R

const val INCOMES_ROUTE = "incomes"

sealed class IncomesDestinations(
    val route: String,
    val title: Int
) {
    data object IncomesScreen : IncomesDestinations(
        INCOMES_ROUTE + "_screen",
        title = R.string.incomes_graph_title
    )

    data object IncomesEditScreen : IncomesDestinations(
        INCOMES_ROUTE + "_edit_screen",
        title = R.string.incomes_edit_title
    )


    data object IncomesHistoryScreen : IncomesDestinations(
        INCOMES_ROUTE + "_history_screen",
        title = R.string.incomes_history_title
    )

    data object IncomesAnalyticScreen : IncomesDestinations(
        INCOMES_ROUTE + "_analytic_screen",
        title = R.string.incomes_analytic_title
    )
}