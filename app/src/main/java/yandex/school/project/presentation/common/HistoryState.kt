package yandex.school.project.presentation.common

import yandex.school.project.domain.entities.TransactionWithCategory
import java.time.LocalDate

/**
 * Состояние для экранов истории транзакций.
 * Единственная ответственность: хранение данных о состоянии UI для истории транзакций.
 */
data class HistoryState(
    val transactions: List<TransactionWithCategory>,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val totalAmount: Double,
    val errorMessage: String? = null
) 