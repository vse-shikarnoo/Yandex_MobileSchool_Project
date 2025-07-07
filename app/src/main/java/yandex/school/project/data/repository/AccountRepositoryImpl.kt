package yandex.school.project.data.repository

import yandex.school.project.data.network.ApiService
import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.entities.AccountHistory
import yandex.school.project.domain.repositories.AccountRepository
import javax.inject.Inject
import yandex.school.project.data.mappers.toDomain

/**
 * Реализация репозитория для работы с аккаунтами, связывающая домен с данными.
 * Единственная ответственность: управление данными аккаунтов и преобразование между слоями данных.
 */
class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AccountRepository {
    
    override suspend fun getAccounts(): List<Account> {
        return apiService.getAccounts().map { it.toDomain() }
    }
    
    override suspend fun getAccountById(id: Int): Account {
        return apiService.getAccountById(id).toDomain()
    }
    
    override suspend fun createAccount(name: String, balance: Double, currency: String): Account {
        val request = yandex.school.project.data.models.AccountCreateRequest(
            name = name,
            balance = balance,
            currency = currency
        )
        return apiService.createAccount(request).toDomain()
    }
    
    override suspend fun updateAccount(id: Int, name: String, balance: Double, currency: String): Account {
        val request = yandex.school.project.data.models.AccountUpdateRequest(
            name = name,
            balance = balance,
            currency = currency
        )
        return apiService.updateAccount(id, request).toDomain()
    }
    
    override suspend fun deleteAccount(id: Int) {
        apiService.deleteAccount(id)
    }

    override suspend fun getAccountHistory(id: Int): AccountHistory {
        TODO("Not yet implemented")
    }
} 