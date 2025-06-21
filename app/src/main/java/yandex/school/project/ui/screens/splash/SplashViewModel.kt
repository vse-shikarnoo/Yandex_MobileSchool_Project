package yandex.school.project.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import yandex.school.project.data.repository.AccountRepository
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.models.Account

class SplashViewModel(
    private val accountRepository: AccountRepository = AccountRepository(ApiService(ApiClient()))
) : ViewModel() {

    var accountId: Int = 1
        private set

    init {
        viewModelScope.launch {
            val accounts = try {
                accountRepository.getAccounts()
            } catch (e: Exception) {
                emptyList<Account>()
            }
            //пока лень забивать транзакции через сваггер, но так это есть
            accountId = 1
            //accountId = accounts.firstOrNull()?.id ?: 1
        }
    }
} 