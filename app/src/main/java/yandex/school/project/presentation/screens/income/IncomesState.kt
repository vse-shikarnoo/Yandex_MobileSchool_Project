package yandex.school.project.presentation.screens.income

import yandex.school.project.domain.entities.TransactionWithCategory
import yandex.school.project.presentation.utils.BALANCE_ZERO

data class IncomesState(
    val transactions: List<TransactionWithCategory> = emptyList(),
    val total: String = BALANCE_ZERO
) 