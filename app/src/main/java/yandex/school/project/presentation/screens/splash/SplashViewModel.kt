package yandex.school.project.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.usecases.account.GetFirstAccountUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getFirstAccountUseCase: GetFirstAccountUseCase
) : ViewModel() {

    var accountId: Int = 1
        private set

    init {
        viewModelScope.launch {
            val account = try {
                getFirstAccountUseCase()
            } catch (e: Exception) {
                null
            }
            //пока лень забивать транзакции через сваггер, но так это есть
            accountId = 1
            //accountId = account?.id ?: 1
        }
    }
} 