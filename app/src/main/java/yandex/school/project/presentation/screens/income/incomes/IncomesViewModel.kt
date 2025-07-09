package yandex.school.project.presentation.screens.income.incomes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.core.utils.NetworkOperationHelper
import yandex.school.project.core.utils.Result
import yandex.school.project.presentation.screens.income.IncomesState
import yandex.school.project.core.utils.CURRENCY_RUB
import javax.inject.Inject

/**
 * ViewModel для экрана доходов, управляющий состоянием и загрузкой данных о доходах.
 * Единственная ответственность: управление состоянием UI и загрузка данных о доходах с категориями.
 */
class IncomesViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase,
    private val networkHelper: yandex.school.project.core.utils.NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<yandex.school.project.core.utils.Result<IncomesState>>(
        yandex.school.project.core.utils.Result.Loading)
    val uiState: StateFlow<yandex.school.project.core.utils.Result<IncomesState>> = _uiState

    override fun onCleared() {
        super.onCleared()
        Log.d("${this::class.java}", "onCleared: ")
    }

    fun loadTransactionsWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        viewModelScope.launch {
            networkHelper.executeWithRetry(
                scope = this,
                operation = {
                    val allTransactions = getTransactionsByAccountUseCase(accountId)
                    val categories = getCategoriesUseCase()
                    
                    val incomeTransactions = allTransactions.filter { transaction ->
                        transaction.type == yandex.school.project.core.domain.entities.TransactionType.INCOME
                    }
                    
                    val transactionsWithCategory = incomeTransactions.mapNotNull { transaction ->
                        val category = categories.find { it.id == transaction.categoryId }
                        category?.let {
                            yandex.school.project.core.domain.entities.TransactionWithCategory(
                                transaction,
                                it
                            )
                        }
                    }.sortedByDescending {
                        it.date
                    }
                    
                    val total = transactionsWithCategory.sumOf { it.amount }
                    IncomesState(
                        transactions = transactionsWithCategory,
                        total = "${total.toInt()} ${yandex.school.project.core.utils.CURRENCY_RUB}"
                    )
                },
                onSuccess = { incomesState ->
                    _uiState.value = yandex.school.project.core.utils.Result.Success(incomesState)
                },
                onError = { errorMessage ->
                    _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage)
                },
                maxRetries = maxRetries,
                delayMillis = delayMillis,
                operationName = "загрузка доходов"
            )
        }
    }
} 