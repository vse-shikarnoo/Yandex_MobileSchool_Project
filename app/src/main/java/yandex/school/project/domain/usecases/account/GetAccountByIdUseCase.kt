package yandex.school.project.domain.usecases.account

import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.repositories.AccountRepository
import javax.inject.Inject

/**
 * Use case для получения аккаунта по его идентификатору.
 * Единственная ответственность: получение конкретного аккаунта по ID из репозитория.
 */
class GetAccountByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Int): Account {
        return accountRepository.getAccountById(id)
    }
} 