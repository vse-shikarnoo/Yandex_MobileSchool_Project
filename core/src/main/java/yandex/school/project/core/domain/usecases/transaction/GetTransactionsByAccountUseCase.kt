package yandex.school.project.core.domain.usecases.transaction

import kotlinx.coroutines.flow.Flow
import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Inject

/**
 * Use case для получения транзакций по аккаунту.
 * Единственная ответственность: получение списка транзакций для конкретного аккаунта из репозитория.
 */
class GetTransactionsByAccountUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Int): Flow<List<Transaction>> {
        return transactionRepository.getTransactionsByAccountPeriod(accountId)
    }
} 