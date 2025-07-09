package yandex.school.project.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.usecases.account.GetFirstAccountUseCase
import yandex.school.project.core.utils.CURRENCY_RUB
import javax.inject.Inject

/**
 * ViewModel для экрана загрузки, отвечающий за инициализацию приложения.
 * Единственная ответственность: получение первого аккаунта для инициализации приложения.
 */
class SplashViewModel @Inject constructor(
    private val getFirstAccountUseCase: yandex.school.project.core.domain.usecases.account.GetFirstAccountUseCase
) : ViewModel() {

    var account: yandex.school.project.core.domain.entities.Account? = null
        private set

    var currency: String = yandex.school.project.core.utils.CURRENCY_RUB
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