package yandex.school.project.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import yandex.school.project.core.data.local.dao.AccountDao
import yandex.school.project.core.data.mappers.toCreateRequest
import yandex.school.project.core.data.mappers.toData
import yandex.school.project.core.data.mappers.toDomain
import yandex.school.project.core.data.mappers.toEntity
import yandex.school.project.core.data.network.ApiService
import yandex.school.project.core.domain.entities.Account
import yandex.school.project.core.domain.entities.AccountHistory
import yandex.school.project.core.domain.repositories.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val accountDao: AccountDao
) : AccountRepository {


    // Получение аккаунтов — всегда из локальной базы
    override suspend fun getAccounts(): Flow<List<Account>> {
        return accountDao.getAll().map { list -> list.map { it.toDomain() } }
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
        accountDao.insert(account.toEntity(isSynced = false))
        return account // Возвращаем локальный объект, синхронизация позже
    }

    // Аналогично для update и delete
    override suspend fun updateAccount(id: Int, name: String, balance: Double, currency: String): Account {
        val account = Account(
            id = id,
            name = name,
            balance = balance,
            currency = currency,
            createdAt = "",
            updatedAt = ""
        )
        accountDao.update(account.toEntity(isSynced = false))
        return account
    }

    override suspend fun deleteAccount(id: Int) {
        val entity = accountDao.getById(id)
        if (entity != null) {
            accountDao.delete(entity)
        }
    }

    override suspend fun getAccountHistory(id: Int): AccountHistory {
        TODO("Not yet implemented")
    }

    // Синхронизация с сервером (вызывать через WorkManager или вручную)
    suspend fun syncAccounts() {
        val unsynced = accountDao.getUnsynced()
        for (entity in unsynced) {
            try {
                val data = entity.toDomain().toData().toCreateRequest()
                apiService.createAccount(data)
                accountDao.update(entity.copy(isSynced = true))
            } catch (e: Exception) {
                // Логируем ошибку, повторим позже
            }
        }
    }
}