package yandex.school.project.core.data.repository

import android.util.Log
import yandex.school.project.core.data.network.ApiService
import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Inject
import yandex.school.project.core.data.mappers.toDomain

/**
 * Реализация репозитория для работы с транзакциями, связывающая домен с данными.
 * Единственная ответственность: управление данными транзакций и преобразование между слоями данных.
 */
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
    ) {
        val request = yandex.school.project.core.data.models.TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = date,
            comment = description
        )
    }
    
    override suspend fun getTransactionById(id: Int): Transaction {
        return apiService.getTransactionById(id).toDomain()
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
        val request = yandex.school.project.core.data.models.TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = date,
            comment = description
        )
        return apiService.updateTransaction(id, request).toDomain()
    }
    
    override suspend fun deleteTransaction(id: Int) {
        apiService.deleteTransaction(id)
    }
    
    override suspend fun getTransactionsByAccountPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<Transaction> {
        val t = apiService.getTransactionsByAccountPeriod(accountId, startDate, endDate)
            .map { it.toDomain() }
        Log.d("TransactionRepository", "getTransactionsByAccountPeriod: $t")
        return t
    }
} 