package yandex.school.project.data.repository

import yandex.school.project.data.mappers.TransactionMapper
import yandex.school.project.data.network.ApiService
import yandex.school.project.domain.entities.Transaction
import yandex.school.project.domain.entities.TransactionType
import yandex.school.project.domain.repositories.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TransactionRepository {
    
    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: String,
        type: TransactionType
    ): Transaction {
        val request = yandex.school.project.data.models.TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount.toString(),
            transactionDate = date,
            comment = description
        )
        return TransactionMapper.mapToDomain(apiService.createTransaction(request))
    }
    
    override suspend fun getTransactionById(id: Int): Transaction {
        return TransactionMapper.mapToDomain(apiService.getTransactionById(id))
    }
    
    override suspend fun updateTransaction(
        id: Int,
        accountId: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: String,
        type: TransactionType
    ): Transaction {
        val request = yandex.school.project.data.models.TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount.toString(),
            transactionDate = date,
            comment = description
        )
        return TransactionMapper.mapToDomain(apiService.updateTransaction(id, request))
    }
    
    override suspend fun deleteTransaction(id: Int) {
        apiService.deleteTransaction(id)
    }
    
    override suspend fun getTransactionsByAccountPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<Transaction> {
        return apiService.getTransactionsByAccountPeriod(accountId, startDate, endDate)

            .map { TransactionMapper.mapToDomain(it) }
    }
} 