package yandex.school.project.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.usecases.account.GetFirstAccountUseCase
import yandex.school.project.core.utils.CURRENCY_RUB
import yandex.school.project.core.utils.Result
import javax.inject.Inject

/**
 * ViewModel для экрана загрузки, отвечающий за инициализацию приложения.
 * Единственная ответственность: получение первого аккаунта для инициализации приложения.
 */
class SplashViewModel @Inject constructor(
    private val getFirstAccountUseCase: GetFirstAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Result<Account?>>(Result.Loading)
    val uiState: StateFlow<Result<Account?>> = _uiState

    var currency: String = CURRENCY_RUB
        private set

    init {
        viewModelScope.launch {
            try {
                val account = getFirstAccountUseCase()
                _uiState.value = Result.Success(account)
                currency = account?.currency ?: CURRENCY_RUB
            } catch (e: Exception) {
                _uiState.value = Result.Error(e.message ?: "Ошибка загрузки аккаунта")
            }
        }
    }
}