package yandex.school.project.income.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.core.ui.common.HistoryState
import yandex.school.project.core.ui.common.HistoryViewModel
import yandex.school.project.core.utils.NetworkOperationHelper
import yandex.school.project.core.utils.Result
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class IncomesHistoryViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase,
    private val networkHelper: yandex.school.project.core.utils.NetworkOperationHelper
) : ViewModel(), yandex.school.project.core.ui.common.HistoryViewModel {

    override var uiState by mutableStateOf<Result<HistoryState>>(Result.Loading)
        private set

    private var currentStartDate: LocalDate? = null
    private var currentEndDate: LocalDate? = null

    init {
        val now = LocalDate.now()
        currentStartDate = now.withDayOfMonth(1)
        currentEndDate = now
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int, delayMillis: Long) {
        uiState = Result.Loading
        val start = currentStartDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val end = currentEndDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        viewModelScope.launch {
            getTransactionsByAccountUseCase(accountId)
                .combine(getCategoriesUseCase()) { transactions, categories ->
                    val filteredTransactions = transactions.filter { transaction ->
                        val isExpense = transaction.type == TransactionType.INCOME
                        val inDateRange = if (start != null && end != null) {
                            try {
                                val transactionDateTime = OffsetDateTime.parse(transaction.date)
                                val transactionDate = transactionDateTime.toLocalDate()
                                val startDate = LocalDate.parse(start)
                                val endDate = LocalDate.parse(end)
                                !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)
                            } catch (e: Exception) {
                                true
                            }
                        } else {
                            true
                        }
                        isExpense && inDateRange
                    }.sortedByDescending { it.date }
                    val transactionsWithCategory = filteredTransactions.mapNotNull { transaction ->
                        val category = categories.find { it.id == transaction.categoryId }
                        category?.let {
                            TransactionWithCategory(transaction, it)
                        }
                    }
                    val total = transactionsWithCategory.sumOf { it.amount }
                    HistoryState(
                        transactions = transactionsWithCategory,
                        startDate = currentStartDate,
                        endDate = currentEndDate,
                        totalAmount = total
                    )
                }
                .catch { e -> uiState = Result.Error(e.message ?: "Ошибка загрузки") }
                .collect { state -> uiState = Result.Success(state) }
        }
    }

    override fun onDateRangeSelected(accountId: Int, start: LocalDate, end: LocalDate) {
        currentStartDate = start
        currentEndDate = end
        loadTransactionsWithRetry(accountId, 3, 2000)
    }
} 