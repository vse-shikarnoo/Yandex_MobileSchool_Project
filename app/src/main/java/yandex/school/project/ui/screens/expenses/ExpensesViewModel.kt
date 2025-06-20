package yandex.school.project.ui.screens.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.TransactionsRepository
import yandex.school.project.ui.common.Result
import yandex.school.project.ui.screens.income.IncomeState
import yandex.school.project.ui.common.BaseNetworkViewModel

class ExpensesViewModel() : BaseNetworkViewModel() {

    private val repository: TransactionsRepository = TransactionsRepository(ApiService(ApiClient()))

    private val _uiState = MutableStateFlow<Result<IncomeState>>(Result.Loading)
    val uiState: StateFlow<Result<IncomeState>> = _uiState.asStateFlow()

    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        executeWithRetry(
            operation = { 
                val transactions = repository.getTransactions(accountId, isIncome = false)
                val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                IncomeState(
                    transactions = transactions,
                    total = String.format("%.2f ₽", total)
                )
            },
            onSuccess = { incomeState ->
                _uiState.value = Result.Success(incomeState)
            },
            onError = { errorMessage ->
                _uiState.value = Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка расходов"
        )
    }
}