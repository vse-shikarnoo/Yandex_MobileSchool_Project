package yandex.school.project.expenses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.utils.Result
import yandex.school.project.core.utils.NetworkOperationHelper
import yandex.school.project.core.utils.CURRENCY_RUB
import javax.inject.Inject

/**
 * ViewModel для экрана расходов, управляющий состоянием и загрузкой данных о расходах.
 * Единственная ответственность: управление состоянием UI и загрузка данных о расходах с категориями.
 */
class ExpensesViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<ExpensesState>>(
        Result.Loading)
    val uiState: StateFlow<Result<ExpensesState>> = _uiState

    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        Log.d("ExpensesViewModel", "loadTransactionsWithRetry called, accountId = $accountId")
        viewModelScope.launch {
            networkHelper.executeWithRetry(
                scope = this,
                operation = {
                    val allTransactions = getTransactionsByAccountUseCase(accountId)
                    val categories = getCategoriesUseCase()

                    Log.d("ExpensesViewModel", "allTransactions categories: $allTransactions $categories")
                    
                    val expenseTransactions = allTransactions.filter { transaction ->
                        transaction.type == TransactionType.EXPENSE
                    }
                    Log.d("ExpensesViewModel", "expenseTransactions: $expenseTransactions")


                    val transactionsWithCategory = expenseTransactions.mapNotNull { transaction ->
                        val category = categories.find { it.id == transaction.categoryId }
                        category?.let {
                            TransactionWithCategory(
                                transaction,
                                it
                            )
                        }
                    }.sortedByDescending {
                        it.date
                    }
                    Log.d("ExpensesViewModel", "transactionsWithCategory: $transactionsWithCategory")
                    
                    val total = transactionsWithCategory.sumOf { it.amount }
                    Log.d("ExpensesViewModel", "total: $total")
                    ExpensesState(
                        transactions = transactionsWithCategory,
                        total = "${total.toInt()} ${CURRENCY_RUB}"
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
} 