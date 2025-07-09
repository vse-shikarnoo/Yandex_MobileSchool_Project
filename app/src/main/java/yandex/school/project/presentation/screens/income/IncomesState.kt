package yandex.school.project.presentation.screens.income

import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.utils.BALANCE_ZERO

/**
 * Состояние для экрана доходов.
 * Единственная ответственность: хранение данных о состоянии UI для экрана доходов.
 */
data class IncomesState(
    val transactions: List<yandex.school.project.core.domain.entities.TransactionWithCategory> = emptyList(),
    val total: String = yandex.school.project.core.utils.BALANCE_ZERO
) 