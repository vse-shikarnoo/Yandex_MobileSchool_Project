package yandex.school.project.presentation.screens.income.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import yandex.school.project.domain.entities.TransactionWithCategory
import yandex.school.project.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.presentation.common.HistoryState
import yandex.school.project.presentation.common.HistoryViewModel
import yandex.school.project.presentation.common.NetworkOperationHelper
import yandex.school.project.presentation.common.Result
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class IncomesHistoryViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel(), HistoryViewModel {

    override var uiState: Result<HistoryState> by mutableStateOf(Result.Loading)
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
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = {
                val allTransactions = getTransactionsByAccountUseCase(accountId)
                val categories = getCategoriesUseCase()
                val filteredTransactions = allTransactions.filter { transaction ->
                    val isIncome = transaction.type == yandex.school.project.domain.entities.TransactionType.INCOME
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
                    isIncome && inDateRange
                }.sortedByDescending { it.date }
                val transactionsWithCategory = filteredTransactions.mapNotNull { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    category?.let { TransactionWithCategory(transaction, it) }
                }
                val total = transactionsWithCategory.sumOf { it.amount }
                HistoryState(
                    transactions = transactionsWithCategory,
                    startDate = currentStartDate,
                    endDate = currentEndDate,
                    totalAmount = total
                )
            },
            onSuccess = { data ->
                uiState = Result.Success(data)
            },
            onError = { errorMessage ->
                uiState = Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка истории доходов"
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateRangeSelected(accountId: Int, start: LocalDate, end: LocalDate) {
        currentStartDate = start
        currentEndDate = end
        loadTransactionsWithRetry(accountId, 3, 2000)
    }
} 