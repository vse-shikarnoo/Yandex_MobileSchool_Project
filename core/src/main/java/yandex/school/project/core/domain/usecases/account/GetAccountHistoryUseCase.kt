package yandex.school.project.core.domain.usecases.account

import yandex.school.project.core.domain.entities.AccountHistory
import yandex.school.project.core.domain.repositories.AccountRepository
import javax.inject.Inject

class GetAccountHistoryUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(accountId: Int): AccountHistory {
        return repository.getAccountHistory(accountId)
    }
} 