package yandex.school.project.presentation.common

import yandex.school.project.domain.entities.TransactionWithCategory
import java.time.LocalDate

data class HistoryState(
    val transactions: List<TransactionWithCategory>,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val totalAmount: Double,
    val errorMessage: String? = null
) 