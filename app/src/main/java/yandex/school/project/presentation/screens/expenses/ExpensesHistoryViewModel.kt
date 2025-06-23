package yandex.school.project.presentation.screens.expenses

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import yandex.school.project.domain.entities.Transaction
import yandex.school.project.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.presentation.common.BaseNetworkViewModel
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExpensesHistoryViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseNetworkViewModel() {

    var transactions by mutableStateOf<List<TransactionWithCategory>>(emptyList())
        private set

    var startDate by mutableStateOf<LocalDate?>(null)
    var endDate by mutableStateOf<LocalDate?>(null)
    var totalAmount by mutableStateOf(0.0)
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        val now = LocalDate.now()
        startDate = now.withDayOfMonth(1)
        endDate = now
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        val start = startDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val end = endDate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        errorMessage = null
        
        executeWithRetry(
            operation = { 
                val allTransactions = getTransactionsByAccountUseCase(accountId)
                val categories = getCategoriesUseCase()
                
                val filteredTransactions = allTransactions.filter { transaction ->
                    val isExpense = transaction.type == yandex.school.project.domain.entities.TransactionType.EXPENSE
                    val inDateRange = if (start != null && end != null) {
                        try {
                            // Парсим ISO 8601 дату с временем и часовым поясом
                            val transactionDateTime = OffsetDateTime.parse(transaction.date)
                            val transactionDate = transactionDateTime.toLocalDate()
                            val startDate = LocalDate.parse(start)
                            val endDate = LocalDate.parse(end)
                            !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)
                        } catch (e: Exception) {
                            Log.e("ExpensesHistoryViewModel", "Ошибка парсинга даты: ${transaction.date}", e)
                            true // Если не удается распарсить дату, включаем транзакцию
                        }
                    } else {
                        true
                    }
                    isExpense && inDateRange
                }.sortedByDescending {
                    it.date
                }
                
                // Создаем TransactionWithCategory для каждой транзакции
                filteredTransactions.mapNotNull { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    category?.let { TransactionWithCategory(transaction, it) }
                }
            },
            onSuccess = { result ->
                transactions = result
                totalAmount = result.sumOf { it.amount }
            },
            onError = { errorMessage ->
                this.errorMessage = errorMessage
                transactions = emptyList()
                totalAmount = 0.0
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка истории расходов"
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateRangeSelected(accountId: Int, start: LocalDate, end: LocalDate) {
        startDate = start
        endDate = end
        loadTransactionsWithRetry(accountId)
    }
} 