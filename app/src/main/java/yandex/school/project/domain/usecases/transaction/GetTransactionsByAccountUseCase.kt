package yandex.school.project.domain.usecases.transaction

import yandex.school.project.domain.entities.Transaction
import yandex.school.project.domain.repositories.TransactionRepository
import javax.inject.Inject

class GetTransactionsByAccountUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Int): List<Transaction> {
        return transactionRepository.getTransactionsByAccountPeriod(accountId)
    }
} 