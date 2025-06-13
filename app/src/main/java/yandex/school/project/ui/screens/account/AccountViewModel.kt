package yandex.school.project.ui.screens.account

import androidx.lifecycle.ViewModel
import yandex.school.project.domain.models.AccountStateDomain

class AccountViewModel : ViewModel() {
    private val _accountState = AccountStateDomain(
        id = 1,
        name = "Основной счёт",
        balance = "-670 000 ₽",
        currency = "₽"
    )
    
    val accountState: AccountStateDomain
        get() = _accountState
} 