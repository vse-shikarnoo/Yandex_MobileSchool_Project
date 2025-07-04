package yandex.school.project.domain.usecases.account

import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.repositories.AccountRepository
import javax.inject.Inject

class UpdateAccountCurrencyUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Int, currentName: String, currentBalance: Double, newCurrency: String): Account {
        return accountRepository.updateAccount(id, currentName, currentBalance, newCurrency)
    }
} 