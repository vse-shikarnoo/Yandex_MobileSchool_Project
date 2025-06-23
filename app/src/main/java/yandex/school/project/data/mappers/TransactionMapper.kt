package yandex.school.project.data.mappers

import yandex.school.project.data.models.TransactionResponse as DataTransactionResponse
import yandex.school.project.data.models.TransactionRequest as DataTransactionRequest
import yandex.school.project.domain.entities.Transaction as DomainTransaction
import yandex.school.project.domain.entities.TransactionType as DomainTransactionType

object TransactionMapper {
    fun mapToDomain(dataTransaction: DataTransactionResponse): DomainTransaction {
        return DomainTransaction(
            id = dataTransaction.id,
            accountId = dataTransaction.account.id,
            categoryId = dataTransaction.category.id,
            amount = dataTransaction.amount.toDoubleOrNull() ?: 0.0,
            description = dataTransaction.comment,
            date = dataTransaction.transactionDate,
            type = if (dataTransaction.category.isIncome) DomainTransactionType.INCOME else DomainTransactionType.EXPENSE,
            createdAt = dataTransaction.createdAt,
            updatedAt = dataTransaction.updatedAt
        )
    }
    
    fun mapToRequest(domainTransaction: DomainTransaction): DataTransactionRequest {
        return DataTransactionRequest(
            accountId = domainTransaction.accountId,
            categoryId = domainTransaction.categoryId,
            amount = domainTransaction.amount.toString(),
            transactionDate = domainTransaction.date,
            comment = domainTransaction.description
        )
    }
    
    fun mapToData(domainTransaction: DomainTransaction): DataTransactionResponse {
        throw UnsupportedOperationException("Обратный маппинг Transaction не поддерживается")
    }
} 