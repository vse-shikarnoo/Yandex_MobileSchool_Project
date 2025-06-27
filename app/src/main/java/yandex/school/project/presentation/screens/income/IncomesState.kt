package yandex.school.project.presentation.screens.income

import yandex.school.project.domain.entities.TransactionWithCategory
import yandex.school.project.presentation.utils.BALANCE_ZERO

/**
 * Состояние для экрана доходов.
 * Единственная ответственность: хранение данных о состоянии UI для экрана доходов.
 */
data class IncomesState(
    val transactions: List<TransactionWithCategory> = emptyList(),
    val total: String = BALANCE_ZERO
) 