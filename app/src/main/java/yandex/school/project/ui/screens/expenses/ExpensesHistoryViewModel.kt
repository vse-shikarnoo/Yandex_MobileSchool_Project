package yandex.school.project.ui.screens.expenses

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import yandex.school.project.data.models.TransactionResponse
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.TransactionsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpensesHistoryViewModel() : ViewModel() {

    private val repository: TransactionsRepository = TransactionsRepository(ApiService(ApiClient()))

    var transactions by mutableStateOf<List<TransactionResponse>>(emptyList())
        private set

    var startDate by mutableStateOf<LocalDate?>(null)
    var endDate by mutableStateOf<LocalDate?>(null)
    var totalAmount by mutableStateOf(0.0)

    init {
        val now = LocalDate.now()
        startDate = now.withDayOfMonth(1)
        endDate = now
        loadTransactions()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTransactions() {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val start = startDate?.format(formatter)
            val end = endDate?.format(formatter)
            val result = repository.getTransactions(
                accountId = 1,
                startDate = start,
                endDate = end,
                isIncome = false
            )
            transactions = result
            totalAmount = result.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateRangeSelected(start: LocalDate, end: LocalDate) {
        startDate = start
        endDate = end
        loadTransactions()
    }
} 