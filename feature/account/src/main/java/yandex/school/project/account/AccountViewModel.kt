package yandex.school.project.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yandex.school.project.core.domain.usecases.account.GetAccountByIdUseCase
import yandex.school.project.core.domain.usecases.account.UpdateAccountNameUseCase
import yandex.school.project.core.domain.usecases.account.UpdateAccountBalanceUseCase
import yandex.school.project.core.domain.usecases.account.UpdateAccountCurrencyUseCase
import yandex.school.project.core.utils.NetworkOperationHelper
import javax.inject.Inject

/**
 * ViewModel для экрана аккаунта, управляющий состоянием и загрузкой данных аккаунта.
 * Единственная ответственность: управление состоянием UI и загрузка данных конкретного аккаунта.
 */
class AccountViewModel @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountNameUseCase: UpdateAccountNameUseCase,
    private val updateAccountBalanceUseCase: UpdateAccountBalanceUseCase,
    private val updateAccountCurrencyUseCase: UpdateAccountCurrencyUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<yandex.school.project.core.utils.Result<yandex.school.project.core.domain.entities.Account>>(
        yandex.school.project.core.utils.Result.Loading)
    val uiState: StateFlow<yandex.school.project.core.utils.Result<yandex.school.project.core.domain.entities.Account>> = _uiState

    override fun onCleared() {
        super.onCleared()
        Log.d("${this::class.java}", "onCleared: ")
    }

    fun loadAccount(accountId: Int) {
        networkHelper.executeOnce(
            scope = viewModelScope,
            operation = { getAccountByIdUseCase(accountId) },
            onSuccess = { account ->
                _uiState.value = yandex.school.project.core.utils.Result.Success(account)
            },
            onError = { errorMessage ->
                _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage)
            },
            operationName = "загрузка аккаунта"
        )
    }

    fun loadAccountWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { getAccountByIdUseCase(accountId) },
            onSuccess = { account ->
                _uiState.value = yandex.school.project.core.utils.Result.Success(account)
            },
            onError = { errorMessage ->
                _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка аккаунта"
        )
    }

    fun updateAccountName(newName: String) {
        val account = (uiState.value as? yandex.school.project.core.utils.Result.Success)?.data ?: return
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { updateAccountNameUseCase(account.id, newName, account.balance, account.currency) },
            onSuccess = { acc -> _uiState.value = yandex.school.project.core.utils.Result.Success(acc) },
            onError = { errorMessage -> _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage) },
            operationName = "обновление имени аккаунта ${account.id}, ${newName}, ${account.balance}, ${account.currency}"
        )
    }

    fun updateAccountBalance(newBalance: Double) {
        val account = (uiState.value as? yandex.school.project.core.utils.Result.Success)?.data ?: return

        // Оптимистичное обновление UI
        _uiState.value = yandex.school.project.core.utils.Result.Success(account.copy(balance = newBalance))

        // Асинхронное обновление на сервере
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { updateAccountBalanceUseCase(account.id, account.name, newBalance, account.currency) },
            onSuccess = { acc -> _uiState.value = yandex.school.project.core.utils.Result.Success(acc) },
            onError = { errorMessage -> _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage) },
            operationName = "обновление баланса аккаунта ${account.id}, ${account.name}, $newBalance, ${account.currency}"
        )
    }

    fun updateAccountCurrency(newCurrency: String) {
        val account = (uiState.value as? yandex.school.project.core.utils.Result.Success)?.data ?: return
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { updateAccountCurrencyUseCase(account.id, account.name, account.balance, newCurrency) },
            onSuccess = { acc -> _uiState.value = yandex.school.project.core.utils.Result.Success(acc) },
            onError = { errorMessage -> _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage) },
            operationName = "обновление валюты аккаунта ${account.id}, ${account.name}, ${account.balance}, ${newCurrency}"
        )
    }
} 