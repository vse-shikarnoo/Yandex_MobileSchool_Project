package yandex.school.project.presentation.screens.expenses

import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.utils.BALANCE_ZERO

/**
 * Состояние для экрана расходов.
 * Единственная ответственность: хранение данных о состоянии UI для экрана расходов.
 */
data class ExpensesState(
    val transactions: List<yandex.school.project.core.domain.entities.TransactionWithCategory> = emptyList(),
    val total: String = yandex.school.project.core.utils.BALANCE_ZERO
) 