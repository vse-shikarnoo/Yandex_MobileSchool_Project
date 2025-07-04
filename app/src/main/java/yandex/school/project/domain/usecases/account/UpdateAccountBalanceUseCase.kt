package yandex.school.project.domain.usecases.account

import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.repositories.AccountRepository
import javax.inject.Inject

class UpdateAccountBalanceUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Int, currentName: String, newBalance: Double, currentCurrency: String): Account {
        return accountRepository.updateAccount(id, currentName, newBalance, currentCurrency)
    }
} 