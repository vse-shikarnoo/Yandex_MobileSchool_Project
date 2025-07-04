package yandex.school.project.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.usecases.account.GetFirstAccountUseCase
import yandex.school.project.presentation.utils.CURRENCY_RUB
import javax.inject.Inject

/**
 * ViewModel для экрана загрузки, отвечающий за инициализацию приложения.
 * Единственная ответственность: получение первого аккаунта для инициализации приложения.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getFirstAccountUseCase: GetFirstAccountUseCase
) : ViewModel() {

    var account: Account? = null
        private set

    var currency: String = CURRENCY_RUB
        private set

    init {
        viewModelScope.launch {
            account = try {
                getFirstAccountUseCase()
            } catch (e: Exception) {
                null
            }
        }
    }
} 