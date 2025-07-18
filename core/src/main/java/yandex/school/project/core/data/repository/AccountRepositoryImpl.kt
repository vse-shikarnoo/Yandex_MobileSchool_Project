package yandex.school.project.core.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import yandex.school.project.core.data.local.dao.AccountDao
import yandex.school.project.core.data.mappers.toCreateRequest
import yandex.school.project.core.data.mappers.toData
import yandex.school.project.core.data.mappers.toDomain
import yandex.school.project.core.data.mappers.toEntity
import yandex.school.project.core.data.models.AccountUpdateRequest
import yandex.school.project.core.data.network.ApiService
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.entities.AccountHistory
import yandex.school.project.core.domain.repositories.AccountRepository
import javax.inject.Inject
import yandex.school.project.core.utils.NetworkOperationHelper

private const val TAG = "ACCOUNT_REPOSITORY_IMPL"

class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val accountDao: AccountDao,
    private val networkHelper: NetworkOperationHelper // или другой helper для проверки сети
) : AccountRepository {

    override suspend fun getAccounts(): Flow<List<Account>> = flow {
        try {
            val remoteAccounts = apiService.getAccounts()
            Log.d(TAG, "getAccounts: $remoteAccounts")
            accountDao.insertAll(remoteAccounts.map { it.toDomain().toEntity(isSynced = true, syncAction = null) })
        } catch (e: Exception) {
            // Логируем ошибку, но не прерываем поток
        }
        emitAll(accountDao.getAll().map { list -> list.map { it.toDomain() } })
    }

    override suspend fun getAccountById(id: Int): Account {
        val entity = accountDao.getById(id)
        return entity?.toDomain() ?: throw IllegalArgumentException("Account not found")
    }

    // Создание аккаунта — сохраняем локально, синхронизация отдельно
    override suspend fun createAccount(name: String, balance: Double, currency: String): Account {
        val account = Account(
            id = 0,
            name = name,
            balance = balance,
            currency = currency,
            createdAt = "",
            updatedAt = ""
        )
        try {
            val response = apiService.createAccount(account.toData().toCreateRequest())
            // Если успешно — сохраняем как синхронизированную с серверным id
            accountDao.insert(account.copy(id = response.id).toEntity(isSynced = true, syncAction = null))
            return account.copy(id = response.id)
        } catch (e: Exception) {
            // Если ошибка — сохраняем как не синхронизированную с временным id
            accountDao.insert(account.toEntity(isSynced = false, syncAction = "CREATE"))
            return account
        }
    }


    override suspend fun updateAccount(id: Int, name: String, balance: Double, currency: String): Account {
        val account = Account(
            id = id,
            name = name,
            balance = balance,
            currency = currency,
            createdAt = "",
            updatedAt = ""
        )
        try {
            apiService.updateAccount(id, AccountUpdateRequest(name, balance, currency))
            accountDao.update(account.toEntity(isSynced = true, syncAction = null))
        } catch (e: Exception) {
            accountDao.update(account.toEntity(isSynced = false, syncAction = "UPDATE"))
        }
        return account
    }

    override suspend fun deleteAccount(id: Int) {
        val entity = accountDao.getById(id)
        if (entity != null) {
            try {
                apiService.deleteAccount(id)
                accountDao.delete(entity) // Удаляем из базы, если успешно
            } catch (e: Exception) {
                // Soft delete: помечаем для синхронизации
                accountDao.update(entity.copy(isSynced = false, syncAction = "DELETE"))
            }
        }
    }

    override suspend fun getAccountHistory(id: Int): AccountHistory {
        TODO("Not yet implemented")
    }

    // Синхронизация с сервером (вызывать через WorkManager или вручную)
    suspend fun syncAccounts() {
        val unsynced = accountDao.getUnsynced()
        Log.d(TAG, "Начало синхронизации аккаунтов. Несинхронизированных: ${unsynced.size}")
        for (entity in unsynced) {
            try {
                when (entity.syncAction) {
                    "CREATE" -> {
                        Log.d(TAG, "CREATE: локальный id=${entity.id}, данные=${entity}")
                        val response = apiService.createAccount(entity.toDomain().toData().toCreateRequest())
                        // response.id — id с сервера
                        accountDao.delete(entity)
                        val syncedEntity = entity.copy(
                            id = response.id,
                            isSynced = true,
                            syncAction = null
                        )
                        accountDao.insert(syncedEntity)
                        Log.d(TAG, "CREATE: запись обновлена в БД с серверным id=${response.id}")
                    }
                    "UPDATE" -> {
                        Log.d(TAG, "UPDATE: id=${entity.id}, данные=${entity}")
                        apiService.updateAccount(entity.id, AccountUpdateRequest(entity.name, entity.balance, entity.currency))
                        accountDao.update(entity.copy(isSynced = true, syncAction = null))
                        Log.d(TAG, "UPDATE: запись обновлена в БД id=${entity.id}")
                    }
                    "DELETE" -> {
                        Log.d(TAG, "DELETE: id=${entity.id}, данные=${entity}")
                        apiService.deleteAccount(entity.id)
                        accountDao.delete(entity)
                        Log.d(TAG, "DELETE: запись удалена из БД id=${entity.id}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при синхронизации id=${entity.id}, action=${entity.syncAction}", e)
            }
        }
        Log.d(TAG, "Синхронизация аккаунтов завершена")
    }
}