package yandex.school.project.core.domain.usecases.transaction

import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Inject

/**
 * Use case для создания новой транзакции.
 * Единственная ответственность: создание транзакции через репозиторий.
 */
class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        transaction: Transaction
    ) {
        return transactionRepository.createTransaction(
            accountId = transaction.accountId,
            categoryId = transaction.categoryId,
            amount = transaction.amount,
            description = transaction.description,
            date = transaction.date,
            type = transaction.type
        )
    }
} 