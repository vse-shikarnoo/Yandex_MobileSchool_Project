package yandex.school.project.core.domain.usecases.account

import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.repositories.AccountRepository
import javax.inject.Inject

/**
 * Use case для получения списка всех аккаунтов.
 * Единственная ответственность: получение списка аккаунтов из репозитория.
 */
class GetAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): List<Account> {
        return accountRepository.getAccounts()
    }
}