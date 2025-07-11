package yandex.school.project.core.domain.usecases.transaction

import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        id: Int
    ) {
        return transactionRepository.deleteTransaction(
            id = id
        )
    }
}