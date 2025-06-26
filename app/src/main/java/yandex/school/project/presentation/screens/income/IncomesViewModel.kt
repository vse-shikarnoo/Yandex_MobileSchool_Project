package yandex.school.project.presentation.screens.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yandex.school.project.domain.entities.TransactionWithCategory
import yandex.school.project.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.presentation.common.NetworkOperationHelper
import yandex.school.project.presentation.common.Result
import yandex.school.project.presentation.utils.CURRENCY_RUB
import javax.inject.Inject

@HiltViewModel
class IncomesViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<IncomesState>>(Result.Loading)
    val uiState: StateFlow<Result<IncomesState>> = _uiState

    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = {
                val allTransactions = getTransactionsByAccountUseCase(accountId)
                val categories = getCategoriesUseCase()
                
                val incomeTransactions = allTransactions.filter { transaction ->
                    transaction.type == yandex.school.project.domain.entities.TransactionType.INCOME
                }
                
                val transactionsWithCategory = incomeTransactions.mapNotNull { transaction ->
                    val category = categories.find { it.id == transaction.categoryId }
                    category?.let { TransactionWithCategory(transaction, it) }
                }.sortedByDescending {
                    it.date
                }
                
                val total = transactionsWithCategory.sumOf { it.amount }
                IncomesState(
                    transactions = transactionsWithCategory,
                    total = "${total.toInt()} $CURRENCY_RUB"
                )
            },
            onSuccess = { incomesState ->
                _uiState.value = Result.Success(incomesState)
            },
            onError = { errorMessage ->
                _uiState.value = Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка доходов"
        )
    }
} 