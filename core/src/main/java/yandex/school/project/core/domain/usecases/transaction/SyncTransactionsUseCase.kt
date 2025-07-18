package yandex.school.project.core.domain.usecases.transaction

import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Inject

class SyncTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke() {
        transactionRepository.syncTransactions()
    }
}