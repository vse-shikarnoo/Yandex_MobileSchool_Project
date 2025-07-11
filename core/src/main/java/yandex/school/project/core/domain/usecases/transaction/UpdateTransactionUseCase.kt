package yandex.school.project.core.domain.usecases.transaction

import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase@Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        transaction: Transaction
    ): Transaction {
        return transactionRepository.updateTransaction(
            accountId = transaction.accountId,
            categoryId = transaction.categoryId,
            amount = transaction.amount,
            description = transaction.description,
            date = transaction.date,
            type = transaction.type,
            id = transaction.id
        )
    }
}