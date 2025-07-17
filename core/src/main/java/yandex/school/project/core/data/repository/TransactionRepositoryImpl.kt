package yandex.school.project.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import yandex.school.project.core.data.local.dao.TransactionDao
import yandex.school.project.core.data.mappers.toDomain
import yandex.school.project.core.data.mappers.toEntity
import yandex.school.project.core.data.mappers.toRequest
import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.repositories.TransactionRepository
import yandex.school.project.core.data.network.ApiService
import javax.inject.Inject

/**
 * Реализация репозитория для работы с транзакциями, связывающая домен с данными.
 * Единственная ответственность: управление данными транзакций и преобразование между слоями данных.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun getTransactionsByAccountPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Flow<List<Transaction>> {
        return transactionDao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTransactionById(id: Int): Transaction {
        val entity = transactionDao.getById(id)
        return entity?.toDomain() ?: throw IllegalArgumentException("Transaction not found")
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: Double,
        description: String?,
        date: String,
        type: TransactionType
    ) {
        val transaction = Transaction(
            id = 0,
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            description = description,
            date = date,
            type = type,
            createdAt = date,
            updatedAt = date
        )
        transactionDao.insert(transaction.toEntity(isSynced = false))
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
        val transaction = Transaction(
            id = id,
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            description = description,
            date = date,
            type = type,
            createdAt = date,
            updatedAt = date
        )
        transactionDao.update(transaction.toEntity(isSynced = false))
        return transaction
    }

    override suspend fun deleteTransaction(id: Int) {
        val entity = transactionDao.getById(id)
        if (entity != null) {
            transactionDao.delete(entity)
        }
    }

    // Синхронизация с сервером (вызывать через WorkManager или вручную)
    suspend fun syncTransactions() {
        val unsynced = transactionDao.getUnsynced()
        for (entity in unsynced) {
            try {
                val request = entity.toDomain().toRequest()
                apiService.createTransaction(request)
                transactionDao.update(entity.copy(isSynced = true))
            } catch (e: Exception) {
                // Логируем ошибку, повторим позже
            }
        }
    }
} 