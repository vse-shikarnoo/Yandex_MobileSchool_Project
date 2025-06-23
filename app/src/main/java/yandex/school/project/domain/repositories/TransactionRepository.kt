package yandex.school.project.domain.repositories

import yandex.school.project.domain.entities.Transaction

interface TransactionRepository {
    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: String,
        type: yandex.school.project.domain.entities.TransactionType
    ): Transaction
    
    suspend fun getTransactionById(id: Int): Transaction
    suspend fun updateTransaction(
        id: Int,
        accountId: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: String,
        type: yandex.school.project.domain.entities.TransactionType
    ): Transaction
    
    suspend fun deleteTransaction(id: Int)
    suspend fun getTransactionsByAccountPeriod(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): List<Transaction>
} 