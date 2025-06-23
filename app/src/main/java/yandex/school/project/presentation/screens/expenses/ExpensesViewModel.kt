package yandex.school.project.presentation.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yandex.school.project.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.presentation.common.Result
import yandex.school.project.presentation.common.BaseNetworkViewModel
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseNetworkViewModel() {

    private val _uiState = MutableStateFlow<Result<ExpensesState>>(Result.Loading)
    val uiState: StateFlow<Result<ExpensesState>> = _uiState

    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        executeWithRetry(
            operation = {
                val allTransactions = getTransactionsByAccountUseCase(accountId)
                val categories = getCategoriesUseCase()
                
                val expenseTransactions = allTransactions.filter { transaction ->
                    transaction.type == yandex.school.project.domain.entities.TransactionType.EXPENSE
                }
                
                val transactionsWithCategory = expenseTransactions.mapNotNull { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    category?.let { TransactionWithCategory(transaction, it) }
                }.sortedByDescending {
                    it.date
                }
                
                val total = transactionsWithCategory.sumOf { it.amount }
                ExpensesState(
                    transactions = transactionsWithCategory,
                    total = "${total.toInt()} ₽"
                )
            },
            onSuccess = { expensesState ->
                _uiState.value = Result.Success(expensesState)
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