package yandex.school.project.ui.screens.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import yandex.school.project.data.models.AccountResponse
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.repository.AccountRepository
import yandex.school.project.ui.common.Result
import java.io.IOException
import java.net.UnknownHostException

class AccountViewModel() : ViewModel() {

    private val repository: AccountRepository = AccountRepository(ApiService(ApiClient()))

    private val _uiState = MutableStateFlow<Result<AccountResponse>>(Result.Loading)
    val uiState: StateFlow<Result<AccountResponse>> = _uiState

    fun loadAccount(accountId: Int) {
        _uiState.value = Result.Loading
        viewModelScope.launch {
            try {
                val account = repository.getAccountById(accountId)
                _uiState.value = Result.Success(account)
            } catch (e: Exception) {
                _uiState.value = Result.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun loadAccountWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        viewModelScope.launch {
            var attempt = 0
            var success = false
            var lastError: Exception? = null
            _uiState.value = Result.Loading
            while (attempt < maxRetries && !success) {
                try {
                    val account = repository.getAccountById(accountId)
                    _uiState.value = Result.Success(account)
                    success = true
                } catch (e: Exception) {
                    lastError = e
                    attempt++
                    if (e is UnknownHostException || e is IOException) {
                        _uiState.value = Result.Error("Нет подключения к интернету")
                        break
                    }
                    if (attempt < maxRetries) {
                        delay(delayMillis)
                    }
                } finally {
                    Log.d("RetryTest", "loadAccountWithRetry: $attempt")
                }
            }
            if (!success && lastError != null) {
                _uiState.value = Result.Error(lastError.message ?: "Неизвестная ошибка")
            }
        }
    }
} 