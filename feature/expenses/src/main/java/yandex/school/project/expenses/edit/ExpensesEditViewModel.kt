package yandex.school.project.expenses.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.usecases.transaction.CreateTransactionUseCase
import yandex.school.project.core.domain.usecases.transaction.DeleteTransactionUseCase
import yandex.school.project.core.domain.usecases.transaction.GetTransactionByIdUseCase
import yandex.school.project.core.domain.usecases.transaction.UpdateTransactionUseCase
import yandex.school.project.core.utils.NetworkOperationHelper
import yandex.school.project.core.utils.Result
import javax.inject.Inject

data class EditExpenseState(
    val transaction: Transaction? = null,
    val isSuccess: Boolean = false
)

class ExpensesEditViewModel @Inject constructor(
    private val getTransactionUseCase: GetTransactionByIdUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<EditExpenseState>>(Result.Success(data = EditExpenseState()))
    val uiState: StateFlow<Result<EditExpenseState>> = _uiState

    fun loadTransaction(transactionId: Int) {

        viewModelScope.launch {
            networkHelper.executeWithRetry(
                scope = this,
                operation = {
                    val transaction = getTransactionUseCase(transactionId)
                    EditExpenseState(transaction = transaction)
                },
                onSuccess = { state -> _uiState.value = Result.Success(state) },
                onError = { error -> _uiState.value = Result.Error(error) },
                operationName = "загрузка транзакции"
            )
        }
    }

    fun saveTransaction(transaction: Transaction, isEdit: Boolean) {
        Log.d("ExpensesEditViewModel", "saveTransaction: $transaction $isEdit")
        val work = viewModelScope.launch {
            networkHelper.executeWithRetry(
                scope = this,
                operation = {
                    if (isEdit) {
                        updateTransactionUseCase(transaction)
                    } else {
                        createTransactionUseCase(transaction)
                    }
                    EditExpenseState(isSuccess = true)
                },
                onSuccess = { state -> _uiState.value = Result.Success(state) },
                onError = { error -> _uiState.value = Result.Error(error) },
                operationName = if (isEdit) "обновление транзакции" else "создание транзакции"
            )
        }
    }

    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch {
            networkHelper.executeWithRetry(
                scope = this,
                operation = {
                    deleteTransactionUseCase(transactionId)
                    EditExpenseState(isSuccess = true)
                },
                onSuccess = { state -> _uiState.value = Result.Success(state) },
                onError = { error -> _uiState.value = Result.Error(error) },
                operationName = "удаление транзакции"
            )
        }
    }
}