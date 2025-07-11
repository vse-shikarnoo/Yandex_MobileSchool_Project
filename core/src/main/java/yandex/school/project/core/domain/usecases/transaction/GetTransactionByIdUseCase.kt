package yandex.school.project.core.domain.usecases.transaction

import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        id: Int
    ): Transaction {
        return transactionRepository.getTransactionById(
            id = id
        )
    }
}