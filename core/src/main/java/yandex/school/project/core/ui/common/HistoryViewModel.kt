package yandex.school.project.core.ui.common

import java.time.LocalDate

/**
 * Интерфейс для ViewModel истории транзакций (расходы/доходы).
 * Единственная ответственность: определение контракта для ViewModel, управляющих историей транзакций.
 */
interface HistoryViewModel {
    val uiState: yandex.school.project.core.utils.Result<HistoryState>
    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000)
    fun onDateRangeSelected(accountId: Int, start: LocalDate, end: LocalDate)
}