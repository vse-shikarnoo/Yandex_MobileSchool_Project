package yandex.school.project.income.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.domain.usecases.transaction.CreateTransactionUseCase
import yandex.school.project.core.domain.usecases.transaction.DeleteTransactionUseCase
import yandex.school.project.core.domain.usecases.transaction.GetTransactionByIdUseCase
import yandex.school.project.core.domain.usecases.transaction.UpdateTransactionUseCase
import yandex.school.project.core.utils.NetworkOperationHelper
import yandex.school.project.core.utils.Result
import javax.inject.Inject

data class EditIncomeState(
    val transaction: Transaction? = null,
    val isSuccess: Boolean = false
)

class IncomesEditViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getTransactionUseCase: GetTransactionByIdUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<EditIncomeState>>(Result.Success(data = EditIncomeState()))
    val uiState: StateFlow<Result<EditIncomeState>> = _uiState

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .catch { _categories.value = emptyList() }
                .collectLatest { categories ->
                    _categories.value = categories.filter { !it.isIncome }
                }
        }
    }

    fun loadTransaction(transactionId: Int) {
        viewModelScope.launch {
            try {
                val transaction = getTransactionUseCase(transactionId)
                _uiState.value = Result.Success(EditIncomeState(transaction = transaction))
            } catch (e: Exception) {
                _uiState.value = Result.Error(e.message ?: "Ошибка загрузки транзакции")
            }
        }
    }

    fun saveTransaction(transaction: Transaction, isEdit: Boolean) {
        viewModelScope.launch {
            try {
                if (isEdit) {
                    updateTransactionUseCase(transaction)
                } else {
                    createTransactionUseCase(transaction)
                }
                _uiState.value = Result.Success(EditIncomeState(isSuccess = true))
            } catch (e: Exception) {
                _uiState.value = Result.Error(e.message ?: "Ошибка сохранения транзакции")
            }
        }
    }

    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(transactionId)
                _uiState.value = Result.Success(EditIncomeState(isSuccess = true))
            } catch (e: Exception) {
                _uiState.value = Result.Error(e.message ?: "Ошибка удаления транзакции")
            }
        }
    }
}