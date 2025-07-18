package yandex.school.project.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.utils.Result
import yandex.school.project.core.utils.CURRENCY_RUB
import javax.inject.Inject

private const val TAG = "EXPENSES_VIEWMODEL"

class ExpensesViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<ExpensesState>>(Result.Loading)
    val uiState: StateFlow<Result<ExpensesState>> = _uiState

    fun observeExpenses(accountId: Int) {
        viewModelScope.launch {
            // Подписываемся на оба Flow и комбинируем их
            getTransactionsByAccountUseCase(accountId)
                .combine(getCategoriesUseCase()) { transactions, categories ->

                    Log.d(TAG, "observeExpenses: $transactions $categories")
                    val expenseTransactions = transactions.filter { it.type == TransactionType.EXPENSE }
                    val transactionsWithCategory = expenseTransactions.mapNotNull { transaction ->
                        val category = categories.find { it.id == transaction.categoryId }
                        category?.let {
                            TransactionWithCategory(transaction, it)
                        }
                    }.sortedByDescending { it.date }
                    val total = transactionsWithCategory.sumOf { it.amount }
                    ExpensesState(
                        transactions = transactionsWithCategory,
                        total = "${total.toInt()} $CURRENCY_RUB"
                    )
                }
                .catch { e -> _uiState.value = Result.Error(e.message ?: "Ошибка загрузки") }
                .collect { state -> _uiState.value = Result.Success(state) }
        }
    }
}