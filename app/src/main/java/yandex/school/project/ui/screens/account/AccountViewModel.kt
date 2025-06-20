package yandex.school.project.ui.screens.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yandex.school.project.data.models.AccountResponse
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.AccountRepository
import yandex.school.project.ui.common.Result
import yandex.school.project.ui.common.BaseNetworkViewModel

class AccountViewModel() : BaseNetworkViewModel() {

    private val repository: AccountRepository = AccountRepository(ApiService(ApiClient()))

    private val _uiState = MutableStateFlow<Result<AccountResponse>>(Result.Loading)
    val uiState: StateFlow<Result<AccountResponse>> = _uiState

    fun loadAccount(accountId: Int) {
        executeOnce(
            operation = { repository.getAccountById(accountId) },
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
        executeWithRetry(
            operation = { repository.getAccountById(accountId) },
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