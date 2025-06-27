package yandex.school.project.domain.usecases.account

import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.repositories.AccountRepository
import javax.inject.Inject

/**
 * Use case для получения первого доступного аккаунта.
 * Единственная ответственность: получение первого аккаунта из списка всех аккаунтов.
 */
class GetFirstAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Account? {
        val accounts = accountRepository.getAccounts()
        return accounts.firstOrNull()
    }
} 