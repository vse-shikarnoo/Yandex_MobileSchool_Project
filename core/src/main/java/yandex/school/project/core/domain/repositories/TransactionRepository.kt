package yandex.school.project.core.domain.repositories

import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.entities.TransactionType

interface TransactionRepository {
    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: String,
        type: TransactionType
    ): Transaction
    
    suspend fun getTransactionById(id: Int): Transaction
    suspend fun updateTransaction(
        id: Int,
        accountId: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: String,
        type: TransactionType
    ): Transaction
    
    suspend fun deleteTransaction(id: Int)
    suspend fun getTransactionsByAccountPeriod(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): List<Transaction>
} 