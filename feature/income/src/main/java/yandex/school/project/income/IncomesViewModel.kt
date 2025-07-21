package yandex.school.project.income

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.core.utils.NetworkOperationHelper
import yandex.school.project.core.utils.Result
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

    private val _uiState = MutableStateFlow<Result<IncomesState>>(Result.Loading)
    val uiState: StateFlow<Result<IncomesState>> = _uiState

    fun observeExpenses(accountId: Int) {
        viewModelScope.launch {
            // Подписываемся на оба Flow и комбинируем их
            getTransactionsByAccountUseCase(accountId)
                .combine(getCategoriesUseCase()) { transactions, categories ->
                    val expenseTransactions = transactions.filter { it.type == TransactionType.INCOME }
                    val transactionsWithCategory = expenseTransactions.mapNotNull { transaction ->
                        val category = categories.find { it.id == transaction.categoryId }
                        category?.let {
                            TransactionWithCategory(transaction, it)
                        }
                    }.sortedByDescending { it.date }
                    val total = transactionsWithCategory.sumOf { it.amount }
                    IncomesState(
                        transactions = transactionsWithCategory,
                        total = "${total.toInt()} $CURRENCY_RUB"
                    )
                }
                .catch { e -> _uiState.value = Result.Error(e.message ?: "Ошибка загрузки") }
                .collect { state -> _uiState.value = Result.Success(state) }
        }
    }
} 