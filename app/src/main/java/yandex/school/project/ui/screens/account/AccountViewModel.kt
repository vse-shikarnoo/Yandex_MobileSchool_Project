package yandex.school.project.ui.screens.account

import androidx.lifecycle.ViewModel
import yandex.school.project.data.models.AccountState

class AccountViewModel : ViewModel() {
    private val _accountState = AccountState(
        id = 1,
        name = "Основной счёт",
        balance = "-670 000 ₽",
        currency = "₽"
    )
    
    val accountState: AccountState
        get() = _accountState
} 