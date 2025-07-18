package yandex.school.project.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import yandex.school.project.core.data.local.dao.TransactionDao
import yandex.school.project.core.data.mappers.toDomain
import yandex.school.project.core.data.mappers.toEntity
import yandex.school.project.core.data.mappers.toRequest
import yandex.school.project.core.domain.entities.Transaction
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.repositories.TransactionRepository
import yandex.school.project.core.data.network.ApiService
import javax.inject.Inject
import yandex.school.project.core.utils.NetworkOperationHelper

/**
 * Реализация репозитория для работы с транзакциями, связывающая домен с данными.
 * Единственная ответственность: управление данными транзакций и преобразование между слоями данных.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val transactionDao: TransactionDao,
    private val networkHelper: NetworkOperationHelper // или другой helper для проверки сети
) : TransactionRepository {

    override suspend fun getTransactionsByAccountPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Flow<List<Transaction>> = flow {
        try {
            val remoteTransactions =
                apiService.getTransactionsByAccountPeriod(accountId, startDate, endDate)
            // Сохраняем в Room (перезаписываем)
            transactionDao.insertAll(remoteTransactions.map { it.toDomain().toEntity(isSynced = true, syncAction = null) })
        } catch (e: Exception) {
            // Логируем ошибку, но не прерываем поток
        }

        // 2. Всегда отдаём данные из Room
        emitAll(transactionDao.getAll().map { list -> list.map { it.toDomain() } })
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
        try {
            apiService.createTransaction(transaction.toRequest())
            // Если успешно — сохраняем как синхронизированную
            transactionDao.insert(transaction.toEntity(isSynced = true, syncAction = null))
        } catch (e: Exception) {
            // Если ошибка — сохраняем как не синхронизированную
            transactionDao.insert(transaction.toEntity(isSynced = false, syncAction = "CREATE"))
        }
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
        try {
            apiService.updateTransaction(transaction.id, transaction.toRequest())
            transactionDao.update(transaction.toEntity(isSynced = true, syncAction = null))
        } catch (e: Exception) {
            transactionDao.update(transaction.toEntity(isSynced = false, syncAction = "UPDATE"))
        }
        return transaction
    }

    override suspend fun deleteTransaction(id: Int) {
        val entity = transactionDao.getById(id)
        if (entity != null) {
            try {
                apiService.deleteTransaction(id)
                transactionDao.delete(entity) // Удаляем из базы, если успешно
            } catch (e: Exception) {
                // Soft delete: помечаем для синхронизации
                transactionDao.update(entity.copy(isSynced = false, syncAction = "DELETE"))
            }
        }
    }

    // Синхронизация с сервером (вызывать через WorkManager или вручную)
    override suspend fun syncTransactions() {
        val unsynced = transactionDao.getUnsynced()
        android.util.Log.d("TransactionSync", "Начало синхронизации. Несинхронизированных: ${unsynced.size}")
        for (entity in unsynced) {
            try {
                when (entity.syncAction) {
                    "CREATE" -> {
                        android.util.Log.d("TransactionSync", "CREATE: локальный id=${entity.id}, данные=${entity}")
                        val response = apiService.createTransaction(entity.toDomain().toRequest())
                        android.util.Log.d("TransactionSync", "CREATE: серверный id=${response.id}, данные=${entity}")
                        transactionDao.delete(entity)
                        val syncedEntity = entity.copy(
                            id = response.id,
                            isSynced = true,
                            syncAction = null
                        )
                        transactionDao.insert(syncedEntity)
                        android.util.Log.d("TransactionSync", "CREATE: запись обновлена в БД с серверным id=${response.id}")
                    }
                    "UPDATE" -> {
                        android.util.Log.d("TransactionSync", "UPDATE: id=${entity.id}, данные=${entity}")
                        apiService.updateTransaction(entity.id, entity.toDomain().toRequest())
                        transactionDao.update(entity.copy(isSynced = true, syncAction = null))
                        android.util.Log.d("TransactionSync", "UPDATE: запись обновлена в БД id=${entity.id}")
                    }
                    "DELETE" -> {
                        android.util.Log.d("TransactionSync", "DELETE: id=${entity.id}, данные=${entity}")
                        apiService.deleteTransaction(entity.id)
                        transactionDao.delete(entity)
                        android.util.Log.d("TransactionSync", "DELETE: запись удалена из БД id=${entity.id}")
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("TransactionSync", "Ошибка при синхронизации id=${entity.id}, action=${entity.syncAction}", e)
            }
        }
        android.util.Log.d("TransactionSync", "Синхронизация завершена")
    }
} 