package yandex.school.project.core.domain.usecases.account

import android.util.Log
import kotlinx.coroutines.flow.first
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.repositories.AccountRepository
import javax.inject.Inject

/**
 * Use case для получения первого доступного аккаунта.
 * Единственная ответственность: получение первого аккаунта из списка всех аккаунтов.
 */
private const val TAG="GET_FIRST_ACCOUNT_USECASE"

class GetFirstAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Account? {
        val accounts = accountRepository.getAccounts().first()
        Log.d(TAG, "invoke: $accounts")
        return accounts.firstOrNull()
    }
} 