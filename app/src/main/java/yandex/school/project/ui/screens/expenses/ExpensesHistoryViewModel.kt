package yandex.school.project.ui.screens.expenses

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import yandex.school.project.data.models.TransactionResponse
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.TransactionsRepository
import yandex.school.project.ui.common.BaseNetworkViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpensesHistoryViewModel() : BaseNetworkViewModel() {

    private val repository: TransactionsRepository = TransactionsRepository(ApiService(ApiClient()))

    var transactions by mutableStateOf<List<TransactionResponse>>(emptyList())
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
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val start = startDate?.format(formatter)
        val end = endDate?.format(formatter)
        errorMessage = null
        
        executeWithRetry(
            operation = { 
                val result = repository.getTransactions(
                    accountId = accountId,
                    startDate = start,
                    endDate = end,
                    isIncome = false
                )
                result
            },
            onSuccess = { result ->
                transactions = result
                totalAmount = result.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
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