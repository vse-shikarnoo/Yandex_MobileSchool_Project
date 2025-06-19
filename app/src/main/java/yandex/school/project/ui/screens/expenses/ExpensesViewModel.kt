package yandex.school.project.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.TransactionsRepository
import yandex.school.project.ui.common.Result
import yandex.school.project.ui.screens.income.IncomeState

class ExpensesViewModel() : ViewModel() {

    private val repository: TransactionsRepository = TransactionsRepository(ApiService(ApiClient()))

    private val _uiState = MutableStateFlow<Result<IncomeState>>(Result.Loading)
    val uiState: StateFlow<Result<IncomeState>> = _uiState.asStateFlow()

    fun loadTransactions(accountId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = Result.Loading
                val transactions = repository.getTransactions(accountId, isIncome = false)
                val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                _uiState.value = Result.Success(
                    IncomeState(
                        transactions = transactions,
                        total = String.format("%.2f ₽", total)
                    )
                )
            } catch (e: Exception) {
                _uiState.value = Result.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}