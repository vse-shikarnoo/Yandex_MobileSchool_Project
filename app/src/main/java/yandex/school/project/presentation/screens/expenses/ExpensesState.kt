package yandex.school.project.presentation.screens.expenses

import yandex.school.project.domain.entities.TransactionWithCategory
import yandex.school.project.presentation.utils.BALANCE_ZERO

data class ExpensesState(
    val transactions: List<TransactionWithCategory> = emptyList(),
    val total: String = BALANCE_ZERO
) 