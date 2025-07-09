package yandex.school.project.core.domain.usecases.account

import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.repositories.AccountRepository
import javax.inject.Inject

class UpdateAccountNameUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Int, newName: String, currentBalance: Double, currentCurrency: String): Account {
        return accountRepository.updateAccount(id, newName, currentBalance, currentCurrency)
    }
} 