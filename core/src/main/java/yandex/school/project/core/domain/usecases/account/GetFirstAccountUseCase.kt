package yandex.school.project.core.domain.usecases.account

import kotlinx.coroutines.flow.first
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.repositories.AccountRepository
import javax.inject.Inject

/**
 * Use case для получения первого доступного аккаунта.
 * Единственная ответственность: получение первого аккаунта из списка всех аккаунтов.
 */
class GetFirstAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Account? {
        val accounts = accountRepository.getAccounts().first()
        return accounts.firstOrNull()
    }
} 