package yandex.school.project.ui.screens.income

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yandex.school.project.data.models.TransactionResponse
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.TransactionsRepository
import yandex.school.project.ui.common.Result
import java.io.IOException
import java.net.UnknownHostException

data class IncomeState(
    val transactions: List<TransactionResponse> = emptyList(),
    val total: String = "0 ₽"
)

class IncomesViewModel() : ViewModel() {

    private val repository: TransactionsRepository = TransactionsRepository(ApiService(ApiClient()))

    private val _uiState = MutableStateFlow<Result<IncomeState>>(Result.Loading)
    val uiState: StateFlow<Result<IncomeState>> = _uiState.asStateFlow()

    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        viewModelScope.launch {
            var attempt = 0
            var success = false
            var lastError: Exception? = null
            _uiState.value = Result.Loading
            while (attempt < maxRetries && !success) {
                try {
                    val transactions = repository.getTransactions(accountId, isIncome = true)
                    val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                    _uiState.value = Result.Success(
                        IncomeState(
                            transactions = transactions,
                            total = String.format("%.2f ₽", total)
                        )
                    )
                    success = true
                } catch (e: Exception) {
                    lastError = e
                    attempt++
                    if (e is UnknownHostException || e is IOException) {
                        _uiState.value = Result.Error("Нет подключения к интернету")
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
                _uiState.value = Result.Error(lastError.message ?: "Неизвестная ошибка")
            }
        }
    }
}