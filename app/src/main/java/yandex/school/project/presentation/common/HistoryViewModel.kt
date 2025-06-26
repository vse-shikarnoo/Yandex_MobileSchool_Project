package yandex.school.project.presentation.common

import java.time.LocalDate
import yandex.school.project.presentation.common.HistoryState

/**
 * Интерфейс для ViewModel истории транзакций (расходы/доходы).
 */
interface HistoryViewModel {
    val uiState: Result<HistoryState>
    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000)
    fun onDateRangeSelected(accountId: Int, start: LocalDate, end: LocalDate)
}