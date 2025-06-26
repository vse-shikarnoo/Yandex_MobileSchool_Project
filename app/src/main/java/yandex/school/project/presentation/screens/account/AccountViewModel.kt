package yandex.school.project.presentation.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.usecases.account.GetAccountByIdUseCase
import yandex.school.project.presentation.common.NetworkOperationHelper
import yandex.school.project.presentation.common.Result
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<Account>>(Result.Loading)
    val uiState: StateFlow<Result<Account>> = _uiState

    fun loadAccount(accountId: Int) {
        networkHelper.executeOnce(
            scope = viewModelScope,
            operation = { getAccountByIdUseCase(accountId) },
            onSuccess = { account ->
                _uiState.value = Result.Success(account)
            },
            onError = { errorMessage ->
                _uiState.value = Result.Error(errorMessage)
            },
            operationName = "загрузка аккаунта"
        )
    }

    fun loadAccountWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { getAccountByIdUseCase(accountId) },
            onSuccess = { account ->
                _uiState.value = Result.Success(account)
            },
            onError = { errorMessage ->
                _uiState.value = Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка аккаунта"
        )
    }
} 