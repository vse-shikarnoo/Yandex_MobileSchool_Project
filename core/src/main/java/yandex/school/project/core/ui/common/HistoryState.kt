package yandex.school.project.core.ui.common

import java.time.LocalDate

/**
 * Состояние для экранов истории транзакций.
 * Единственная ответственность: хранение данных о состоянии UI для истории транзакций.
 */
data class HistoryState(
    val transactions: List<yandex.school.project.core.domain.entities.TransactionWithCategory>,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val totalAmount: Double,
    val errorMessage: String? = null
) 