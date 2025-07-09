package yandex.school.project.presentation.screens.expenses.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.utils.NetworkOperationHelper
import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.ui.common.HistoryViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import yandex.school.project.core.utils.Result
import yandex.school.project.core.ui.common.HistoryState

class ExpensesHistoryViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase,
    private val networkHelper: yandex.school.project.core.utils.NetworkOperationHelper
) : ViewModel(), yandex.school.project.core.ui.common.HistoryViewModel {

    override var uiState by mutableStateOf<yandex.school.project.core.utils.Result<yandex.school.project.core.ui.common.HistoryState>>(
        yandex.school.project.core.utils.Result.Loading)
        private set

    private var currentStartDate: LocalDate? = null
    private var currentEndDate: LocalDate? = null

    init {
        val now = LocalDate.now()
        currentStartDate = now.withDayOfMonth(1)
        currentEndDate = now
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("${this::class.java}", "onCleared: ")
    }

    override fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int, delayMillis: Long) {
        uiState = yandex.school.project.core.utils.Result.Loading
        val start = currentStartDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val end = currentEndDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = {
                val allTransactions = getTransactionsByAccountUseCase(accountId)
                val categories = getCategoriesUseCase()
                val filteredTransactions = allTransactions.filter { transaction ->
                    val isExpense = transaction.type == yandex.school.project.core.domain.entities.TransactionType.EXPENSE
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
                        yandex.school.project.core.domain.entities.TransactionWithCategory(
                            transaction,
                            it
                        )
                    }
                }
                val total = transactionsWithCategory.sumOf { it.amount }
                yandex.school.project.core.ui.common.HistoryState(
                    transactions = transactionsWithCategory,
                    startDate = currentStartDate,
                    endDate = currentEndDate,
                    totalAmount = total
                )
            },
            onSuccess = { data ->
                uiState = yandex.school.project.core.utils.Result.Success(data)
            },
            onError = { errorMessage ->
                uiState = yandex.school.project.core.utils.Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка истории расходов"
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateRangeSelected(accountId: Int, start: LocalDate, end: LocalDate) {
        currentStartDate = start
        currentEndDate = end
        loadTransactionsWithRetry(accountId, 3, 2000)
    }
} 