package yandex.school.project.ui.screens.income

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import yandex.school.project.data.models.TransactionResponse
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.TransactionsRepository
import yandex.school.project.ui.common.Result
import java.io.IOException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomesHistoryViewModel() : ViewModel() {

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
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val start = startDate?.format(formatter)
            val end = endDate?.format(formatter)
            var attempt = 0
            var success = false
            var lastError: Exception? = null
            errorMessage = null
            while (attempt < maxRetries && !success) {
                try {
                    val result = repository.getTransactions(
                        accountId = accountId,
                        startDate = start,
                        endDate = end,
                        isIncome = true
                    )
                    transactions = result
                    totalAmount = result.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                    success = true
                } catch (e: Exception) {
                    lastError = e
                    attempt++
                    if (e is UnknownHostException || e is IOException) {
                        errorMessage = "Нет подключения к интернету"
                        break
                    }
                    if (attempt < maxRetries) {
                        delay(delayMillis)
                    }
                } finally {
                    Log.d("RetryTest", "loadTransactionsWithRetry: $attempt")
                }
            }
            if (!success && lastError != null) {
                errorMessage = lastError.message ?: "Неизвестная ошибка"
                transactions = emptyList()
                totalAmount = 0.0
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTransactions(accountId: Int) {
        loadTransactionsWithRetry(accountId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateRangeSelected(accountId: Int, start: LocalDate, end: LocalDate) {
        startDate = start
        endDate = end
        loadTransactionsWithRetry(accountId)
    }
} 