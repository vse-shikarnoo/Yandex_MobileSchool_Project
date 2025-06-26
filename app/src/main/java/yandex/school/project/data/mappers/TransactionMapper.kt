package yandex.school.project.data.mappers

import yandex.school.project.data.models.TransactionResponse as DataTransactionResponse
import yandex.school.project.data.models.TransactionRequest as DataTransactionRequest
import yandex.school.project.domain.entities.Transaction as DomainTransaction
import yandex.school.project.domain.entities.TransactionType as DomainTransactionType

/**
 * Extension-функции для преобразования между data и domain Transaction.
 */

fun DataTransactionResponse.toDomain(): DomainTransaction = DomainTransaction(
    id = id,
    accountId = account.id,
    categoryId = category.id,
    amount = amount.toDouble(),
    description = comment,
    date = transactionDate,
    type = if (category.isIncome) DomainTransactionType.INCOME else DomainTransactionType.EXPENSE,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DomainTransaction.toRequest(): DataTransactionRequest = DataTransactionRequest(
    accountId = accountId,
    categoryId = categoryId,
    amount = amount,
    transactionDate = date,
    comment = description
)

fun DataTransactionResponse.toData(): DataTransactionResponse {
    throw UnsupportedOperationException("Обратный маппинг Transaction не поддерживается")
} 