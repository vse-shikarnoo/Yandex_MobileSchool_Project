package yandex.school.project.data.repository

import yandex.school.project.data.mappers.AccountMapper
import yandex.school.project.data.network.ApiService
import yandex.school.project.domain.entities.Account
import yandex.school.project.domain.entities.AccountHistory
import yandex.school.project.domain.repositories.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AccountRepository {
    
    override suspend fun getAccounts(): List<Account> {
        return apiService.getAccounts().map { AccountMapper.mapToDomain(it) }
    }
    
    override suspend fun getAccountById(id: Int): Account {
        return AccountMapper.mapToDomain(apiService.getAccountById(id))
    }
    
    override suspend fun createAccount(name: String, balance: Double, currency: String): Account {
        val request = yandex.school.project.data.models.AccountCreateRequest(
            name = name,
            balance = balance.toString(),
            currency = currency
        )
        return AccountMapper.mapToDomain(apiService.createAccount(request))
    }
    
    override suspend fun updateAccount(id: Int, name: String, balance: Double, currency: String): Account {
        val request = yandex.school.project.data.models.AccountUpdateRequest(
            name = name,
            balance = balance.toString(),
            currency = currency
        )
        return AccountMapper.mapToDomain(apiService.updateAccount(id, request))
    }
    
    override suspend fun deleteAccount(id: Int) {
        apiService.deleteAccount(id)
    }

    override suspend fun getAccountHistory(id: Int): AccountHistory {
        TODO("Not yet implemented")
    }
} 